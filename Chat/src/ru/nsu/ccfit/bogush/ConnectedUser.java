package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;
import ru.nsu.ccfit.bogush.message.types.LoginSuccess;
import ru.nsu.ccfit.bogush.message.types.LogoutSuccess;
import ru.nsu.ccfit.bogush.message.types.UserEntered;
import ru.nsu.ccfit.bogush.message.types.UserLeft;

import java.io.IOException;
import java.net.Socket;

public class ConnectedUser implements Runnable, LostConnectionListener {
	private static final Logger logger = LogManager.getLogger(ConnectedUser.class.getSimpleName());

	private static final int DEFAULT_IN_QUEUE_CAPACITY = 50;
	private static final int DEFAULT_OUT_QUEUE_CAPACITY = 50;

	private Server server;
	private Socket socket;
	private LoginPayload loginPayload;
	private User user;
	private boolean online = false;

	private SocketReader socketReader;
	private SocketWriter socketWriter;

	private Thread thread;

	ConnectedUser(Server server, Socket socket) throws IOException {
		this(server, socket, DEFAULT_IN_QUEUE_CAPACITY, DEFAULT_OUT_QUEUE_CAPACITY);
	}

	private ConnectedUser(Server server, Socket socket,
	                     int inQueueCapacity, int outQueueCapacity) throws IOException {
		logger.trace("Create {}", ConnectedUser.class.getSimpleName());
		this.server = server;
		this.socket = socket;
		SocketMessageStream socketMessageStream = new SocketMessageStream(socket);
		socketReader = new SocketReader(socketMessageStream, this, inQueueCapacity);
		socketWriter = new SocketWriter(socketMessageStream, outQueueCapacity);

		thread = new Thread(this, this.getClass().getSimpleName());
		logger.trace("{} created", ConnectedUser.class.getSimpleName());
	}

	void start() {
		logger.trace("Start {}", ConnectedUser.class.getSimpleName());
		thread.start();
		socketReader.start();
		socketWriter.start();
	}

	@Override
	public void run() {
		ServerMessageHandler serverMessageHandler = new ServerMessageHandler(server, this);
		while (!Thread.interrupted()) {
			try {
				socketReader.read().handleBy(serverMessageHandler);
			} catch (InterruptedException e) {
				logger.trace("Socket reader interrupted");
				break;
			}
		}
	}

	void broadcastToOthers(Message msg) {
		logger.info("Broadcasting to others {}", msg);
		for (ConnectedUser cu : server.getConnectedUsers()) {
			if (!cu.equals(this)) {
				try {
					cu.sendMessage(msg);
				} catch (InterruptedException e) {
					logger.error("Couldn't send {} to {}", msg, cu);
				}
			}
		}
	}

	void sendMessage(Message message) throws InterruptedException {
		socketWriter.write(message);
	}

	void logout(User user) {
		online = false;
		LogoutSuccess msg = new LogoutSuccess("Logged out successfully");
		try {
			sendMessage(msg);
		} catch (InterruptedException e) {
			logger.error("Couldn't send {}", msg);
		}
		broadcastToOthers(new UserLeft(user));
	}

	void stop() {
		logger.trace("Stop {}", ConnectedUser.class.getSimpleName());
		thread.interrupt();
		socketReader.stop();
		socketWriter.stop();
	}

	@Override
	public void lostConnection() {
		if (online) {
			broadcastToOthers(new UserLeft(user));
		}
		server.disconnect(this);
		stop();
	}

	public String getNickname() {
		return loginPayload.getNickname();
	}

	@Override
	public String toString() {
		return getNickname();
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
		return loginPayload.equals(that.loginPayload);
	}

	public User login(LoginPayload loginPayload) {
		this.loginPayload = loginPayload;
		online = true;
		user = new User(loginPayload);
		logger.info("Sending login success message back to {}", user);
		try {
			sendMessage(new LoginSuccess("Logged in successfully"));
		} catch (InterruptedException e) {
			logger.error("Couldn't write success message");
		}
		broadcastToOthers(new UserEntered(user));
		return user;
	}
}
