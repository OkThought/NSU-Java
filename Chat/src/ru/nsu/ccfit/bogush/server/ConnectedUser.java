package ru.nsu.ccfit.bogush.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.*;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.types.*;
import ru.nsu.ccfit.bogush.network.*;
import ru.nsu.ccfit.bogush.serialization.Serializer;

import java.io.IOException;
import java.net.Socket;

public class ConnectedUser implements Runnable, LostConnectionListener {
	private static final Logger logger = LogManager.getLogger(ConnectedUser.class.getSimpleName());

	private static final int DEFAULT_IN_QUEUE_CAPACITY = 50;
	private static final int DEFAULT_OUT_QUEUE_CAPACITY = 50;

	private Server server;
	private Socket socket;
	private boolean online = false;

	private SocketReader socketReader;
	private SocketWriter socketWriter;

	private Thread thread;
	private Session session;
	private User user;

	ConnectedUser(Server server, Socket socket, Serializer<Message> serializer) throws IOException {
		this(server, socket, serializer, DEFAULT_IN_QUEUE_CAPACITY, DEFAULT_OUT_QUEUE_CAPACITY);
	}

	ConnectedUser(Server server, Socket socket, Serializer<Message> serializer,
	              int inQueueCapacity, int outQueueCapacity) throws IOException {
		logger.trace("Create {}", this);
		this.server = server;
		this.socket = socket;
		MessageStream messageStream = new MessageStream(serializer);
		socketReader = new SocketReader(messageStream, this, inQueueCapacity);
		socketWriter = new SocketWriter(messageStream, outQueueCapacity);

		thread = new Thread(this, this.getClass().getSimpleName());
		logger.trace("{} created", ConnectedUser.class.getSimpleName());
	}

	@Override
	public void run() {
		ServerMessageHandler serverMessageHandler = new ServerMessageHandler(this);
		while (!Thread.interrupted()) {
			try {
				socketReader.read().handleBy(serverMessageHandler);
			} catch (InterruptedException e) {
				logger.trace("Socket reader interrupted");
				break;
			}
		}
	}

	void start() {
		logger.trace("Start {}", this);
		thread.start();
		socketReader.start();
		socketWriter.start();
	}

	void login(User user) {
		this.user = user;
		session = new Session(hashCode());
		online = true;
		logger.info("Sending login success message back to {}", user);
		try {
			sendMessage(new LoginSuccess(session));
		} catch (InterruptedException e) {
			logger.error("Failed to write success message");
		}
		broadcastToOthers(new LoginEvent(user));
	}

	void logout() {
		online = false;
		Success msg = new Success();
		try {
			sendMessage(msg);
		} catch (InterruptedException e) {
			logger.error("Failed to send {}", msg);
		}
		broadcastToOthers(new LogoutEvent(getUser()));
	}

	void stop() {
		logger.trace("Stop {}", this);
		thread.interrupt();
		socketReader.stop();
		socketWriter.stop();
	}

	@Override
	public void lostConnection() {
		if (online) {
			broadcastToOthers(new LogoutEvent(getUser()));
		}
		server.disconnect(this);
		stop();
	}

	void sendMessage(Message message) throws InterruptedException {
		socketWriter.write(message);
	}

	void broadcastToOthers(Message msg) {
		logger.info("Broadcasting to others {}", msg);
		for (ConnectedUser cu : server.getConnectedUsers()) {
			if (!cu.equals(this)) {
				try {
					cu.sendMessage(msg);
				} catch (InterruptedException e) {
					logger.error("Failed to send {} to {}", msg, cu);
				}
			}
		}
	}

	void addToHistory(TextMessageRequest message) {
		server.addToHistory(message);
	}

	Session getSession() {
		return session;
	}

	public User getUser() {
		return user;
	}

	User[] getUserList() {
		return server.getUserList();
	}

	String getNickname() {
		return getUser().getName();
	}

	@Override
	public String toString() {
		if (user == null)
			return getClass().getSimpleName();
		return getClass().getSimpleName() + "(\"" + getNickname() + "\")";
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ConnectedUser that = (ConnectedUser) o;

		if (!server.equals(that.server)) return false;
		if (!socket.equals(that.socket)) return false;
		return user.equals(that.user);
	}
}
