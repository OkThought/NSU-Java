package ru.nsu.ccfit.bogush.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketAcceptor implements Runnable {
	private static final Logger logger = LogManager.getLogger(Server.class.getSimpleName());

	private int port;
	private SocketAcceptedListener socketAcceptedListener;
	private ServerSocket serverSocket;
	private Thread thread;

	public SocketAcceptor(int port, SocketAcceptedListener socketAcceptedListener, String name) {
		this.port = port;
		this.socketAcceptedListener = socketAcceptedListener;
		thread = new Thread(this, name);
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			logger.error("Failed to create server socket");
			return;
		}
		if (port == 0) {
			port = serverSocket.getLocalPort();
			logger.info("Port set automatically to {}", port);
		}
		logger.trace("Created socket on port {}", port);
		while (!Thread.interrupted()) {
			try {
				Socket socket = serverSocket.accept();
				logger.trace("Accepted socket {}", serverSocket);
				socketAcceptedListener.socketAccepted(socket);
			} catch (IOException e) {
				logger.error("Failed to accept socket: {}", e.getMessage());
				break;
			}
		}
	}

	public void start() {
		logger.trace("Start {}", thread.getName());
		thread.start();
	}

	public void stop() {
		try {
			logger.trace("Closing socket {}", serverSocket);
			serverSocket.close();
			logger.trace("Socket closed");
		} catch (IOException e) {
			logger.error("Failed to close server socket: {}", e.getMessage());
		}
	}

	public interface SocketAcceptedListener {
		void socketAccepted(Socket socket);
	}
}
