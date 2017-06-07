package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	static { LoggingConfiguration.addConfigFile(LoggingConfiguration.DEFAULT_LOGGER_CONFIG_FILE); }

	private static final Logger logger = LogManager.getLogger();

	private static final int PORT = 25565;

	public static void main(String[] args) {
		logger.traceEntry("main");
		Server server = new Server(PORT);
		server.start();
		logger.traceExit("main", null);
	}

	private ArrayList<Connection> connections = new ArrayList<>();
	private int port;

	private Server() {
		this(0);
	}

	private Server(int port) {
		logger.traceEntry("Server");
		this.port = port;
		logger.traceExit("Server", null);
	}

	private void start() {
		logger.traceEntry("start");
		try (ServerSocket serverSocket = new ServerSocket(port)){
			if (port == 0) port = serverSocket.getLocalPort();
			logger.debug("Created socket on port {}", port);
			while (true) {
				Socket socket = serverSocket.accept();
				logger.info("{} connected", socket.getInetAddress().getHostAddress());
				Connection connection = new Connection(this, socket);
				connections.add(connection);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			logger.traceExit("start", null);
		}
	}
}
