package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.Message;
import ru.nsu.ccfit.bogush.msg.MessageHandler;

import java.io.IOException;
import java.net.Socket;

public class ConnectedUser {
	private static final Logger logger = LogManager.getLogger();

	private static final int DEFAULT_IN_QUEUE_CAPACITY = 50;
	private static final int DEFAULT_OUT_QUEUE_CAPACITY = 50;

	private Server server;
	private Socket socket;
	private MessageHandler messageHandler;
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
		socketReader = new SocketReader(socketMessageStream, inQueueCapacity);
		socketWriter = new SocketWriter(socketMessageStream, outQueueCapacity);

		thread = new Thread(() -> {
			ServerMessageHandler serverMessageHandler = new ServerMessageHandler(server, this);
			while (!Thread.interrupted()) {
				try {
					socketReader.read().handleBy(serverMessageHandler);
				} catch (InterruptedException e) {
					logger.error("Socket reader interrupted");
					break;
				}
			}
		});
	}

	public void setLoginPayload(LoginPayload loginPayload) {
		this.loginPayload = loginPayload;
	}

	public void sendMessage(Message message) throws InterruptedException {
		socketWriter.write(message);
	}

	public void start() {
		logger.trace("Start");
		socketReader.start();
		socketWriter.start();
	}

	public void stop() {
		logger.trace("Stop");
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
		return loginPayload.getNickname().hashCode();
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
