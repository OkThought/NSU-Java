package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;
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

	private SocketReader socketReader;
	private SocketWriter socketWriter;

	private Thread thread;

	public ConnectedUser(Server server, Socket socket) throws IOException {
		this(server, socket, DEFAULT_IN_QUEUE_CAPACITY, DEFAULT_OUT_QUEUE_CAPACITY);
	}

	public ConnectedUser(Server server, Socket socket,
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

	@Override
	public void run() {
		ServerMessageHandler serverMessageHandler = new ServerMessageHandler(server, this);
		while (!Thread.interrupted()) {
			try {
				socketReader.read().handleBy(serverMessageHandler);
			} catch (InterruptedException e) {
				logger.error("Socket reader interrupted");
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

	@Override
	public void lostConnection() {
		broadcastToOthers(new UserLeft(new User(getNickname())));
		server.disconnect(this);
		stop();
	}

	public void setLoginPayload(LoginPayload loginPayload) {
		this.loginPayload = loginPayload;
	}

	public void sendMessage(Message message) throws InterruptedException {
		socketWriter.write(message);
	}

	public void start() {
		logger.trace("Start {}", ConnectedUser.class.getSimpleName());
		thread.start();
		socketReader.start();
		socketWriter.start();
	}

	public void stop() {
		logger.trace("Stop {}", ConnectedUser.class.getSimpleName());
		thread.interrupt();
		socketReader.stop();
		socketWriter.stop();
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
}
