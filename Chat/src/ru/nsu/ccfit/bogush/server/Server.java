package ru.nsu.ccfit.bogush.server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import ru.nsu.ccfit.bogush.LoggingConfiguration;
import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.types.TextMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

public class Server implements Runnable {
	static { LoggingConfiguration.addConfigFile(LoggingConfiguration.DEFAULT_LOGGER_CONFIG_FILE); }
	private static final Logger logger = LogManager.getLogger(Server.class.getSimpleName());

	private static final String DO_LOGGING_KEY = "log";
	private static final String SERVER_PORT_KEY = "server-port";

	private static final String DO_LOGGING_DEFAULT = "true";
	private static final String SERVER_PORT_DEFAULT = "0";

	private static final String PROPERTIES_FILE = "server.properties";
	private static final String PROPERTIES_COMMENT = "Server properties file";

	private static final Properties DEFAULT_PROPERTIES = new Properties();

	static {
		DEFAULT_PROPERTIES.setProperty(DO_LOGGING_KEY, DO_LOGGING_DEFAULT);
		DEFAULT_PROPERTIES.setProperty(SERVER_PORT_KEY, SERVER_PORT_DEFAULT);
	}

	private static final int HISTORY_CAPACITY = 50;
	private static final String STOP_COMMAND = "stop";

	private ServerSocket serverSocket;

	private Properties properties = new Properties(DEFAULT_PROPERTIES);

	private HashSet<ConnectedUser> connectedUsers = new HashSet<>();

	private ArrayBlockingQueue<TextMessage> history = new ArrayBlockingQueue<>(HISTORY_CAPACITY);

	private int port;

	private Thread thread;

	private static void help() {
		System.out.println("Type 'stop' to stop the server");
	}

	public static void main(String[] args) {
		Server server = new Server();
	}

	private Server() {
		configure();
		thread = new Thread(this, this.getClass().getSimpleName());
		start();
		enterConsoleMode();
	}

	private void enterConsoleMode() {
		logger.info("Entering console mode...\n\n");
		help();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			String command;
			while ((command = reader.readLine()) != null) {
				if (command.trim().toLowerCase().equals(STOP_COMMAND)) {
					logger.trace("Command '{}' received", STOP_COMMAND);
					stop();
					return;
				} else {
					System.out.println("Unknown command");
					help();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("eof found");
			System.exit(1);
		}
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			if (port == 0) {
				port = serverSocket.getLocalPort();
				logger.info("Port set automatically to {}", port);
			}
			logger.info("Created socket on port {}", port);
			logger.info("Server localhost ip: {}", InetAddress.getLocalHost().getHostAddress());
			while (!Thread.interrupted()) {
				logger.info("");
				logger.info("");
				Socket socket = serverSocket.accept();
				logger.info("Socket [{}] accepted", socket.getInetAddress().getHostAddress());
				ConnectedUser connectedUser = new ConnectedUser(this, socket);
				if (connectedUsers.contains(connectedUser)) {
					logger.debug("User already connected");
				} else {
					connectedUsers.add(connectedUser);
					connectedUser.start();
				}
			}
		} catch (SocketException e) {
			logger.info(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			logger.info("Stop server");
			stop();
		}
	}

	private void start() {
		logger.info("Start server");
		thread.start();
		logger.info("Server started");
	}

	private void stop() {
		logger.info("Stopping the server");
		try {
			logger.trace("Closing socket");
			serverSocket.close();
			logger.trace("Socket closed");
		} catch (IOException e) {
			logger.error("Couldn't close server socket: {}", e.getMessage());
		}

		for (ConnectedUser user : connectedUsers) {
			user.stop();
		}
		thread.interrupt();
		logger.info("Server stopped. Exiting...");
	}

	void disconnect(ConnectedUser connectedUser) {
		connectedUsers.remove(connectedUser);
		connectedUser.stop();
	}

	void addToHistory(TextMessage message) {
		logger.trace("Add \"{}\" to history", message.toString());
		try {
			if (history.remainingCapacity() == 0) {
				history.take();
			}
			history.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	HashSet<ConnectedUser> getConnectedUsers() {
		return connectedUsers;
	}

	User[] getUserList() {
		User[] users = new User[connectedUsers.size()];
		Iterator<ConnectedUser> iterator = connectedUsers.iterator();
		for (int i = 0; i < connectedUsers.size(); i++) {
			users[i] = new User(iterator.next().getNickname());
		}
		return users;
	}

	private void configure() {
		logger.info("=== Configuration ===");
		loadProperties();

		boolean doLogging = Boolean.parseBoolean(properties.getProperty(DO_LOGGING_KEY));
		if (!doLogging) {
			Configurator.setRootLevel(Level.OFF);
		}
		port = Integer.parseInt(properties.getProperty(SERVER_PORT_KEY));

		storeProperties();
		logger.info("Exit configuration");
		logger.info("");
	}

	private void loadProperties() {
		logger.info("Looking for properties file \"{}\"...", PROPERTIES_FILE);
		Path path = Paths.get(PROPERTIES_FILE);
		if (Files.exists(path)) {
			logger.info("\"{}\" file found", PROPERTIES_FILE);
			try (InputStream is = new FileInputStream(PROPERTIES_FILE)) {
				logger.info("Loading \"{}\"...", PROPERTIES_FILE);
				properties.load(is);
			} catch (FileNotFoundException e) {
				logger.error("File \"{}\" disappeared! (Shouldn't get here normally)", PROPERTIES_FILE);
				return;
			} catch (IOException e) {
				logger.error("Problems with loading properties file \"{}\"", PROPERTIES_FILE);
				return;
			}
			logger.info("Properties file loaded successfully");
		} else {
			logger.warn("Properties file \"{}\" not found", PROPERTIES_FILE);
			logger.info("Properties file \"{}\" will be created and filled with default values");
		}
	}

	private void storeProperties() {
		// force store each key-value pair
		for (String key : properties.stringPropertyNames()) {
			properties.setProperty(key, properties.getProperty(key));
		}

		try (OutputStream os = new FileOutputStream(PROPERTIES_FILE)) {
			properties.store(os, PROPERTIES_COMMENT);
		} catch (IOException e) {
			logger.error("Problems with storing properties file \"{}\"", PROPERTIES_FILE);
		}
	}
}
