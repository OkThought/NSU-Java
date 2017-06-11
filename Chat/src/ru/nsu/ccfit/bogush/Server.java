package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import ru.nsu.ccfit.bogush.msg.TextMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

public class Server {
	static { LoggingConfiguration.addConfigFile(LoggingConfiguration.DEFAULT_LOGGER_CONFIG_FILE); }
	private static final Logger logger = LogManager.getLogger();

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
		server.start();
		try {
			server.getThread().join();
		} catch (InterruptedException e) {
			logger.trace("Server interrupted");
			e.printStackTrace();
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			help();
			String command;
			while ((command = reader.readLine()) != null) {
				if (command.trim().toLowerCase().equals(STOP_COMMAND)) {
					server.stop();
					return;
				} else {
					System.out.println("Unknown command");
					help();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.error("eof found");
		System.exit(1);
	}

	private Server() {
		configure();
		thread = new Thread(() -> {
			try (ServerSocket serverSocket = new ServerSocket(port)){
				if (port == 0) {
					port = serverSocket.getLocalPort();
					logger.info("Port set automatically to {}", port);
				}
				logger.info("Created socket on port {}", port);
				logger.info("Server localhost ip: {}", InetAddress.getLocalHost().getHostAddress());
				while (!Thread.interrupted()) {
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
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				logger.info("Stop server");
			}
		});
	}

	private Thread getThread() {
		return thread;
	}

	private void start() {
		logger.info("Start server");
		thread.start();
		logger.info("Server started");
	}

	private void stop() {
		for (ConnectedUser user : connectedUsers) {
			user.stop();
		}
		thread.interrupt();
	}

	void logout(ConnectedUser connectedUser) {
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
		logger.traceEntry("configure");
		loadProperties();

		boolean doLogging = Boolean.parseBoolean(properties.getProperty(DO_LOGGING_KEY));
		if (!doLogging) {
			Configurator.setRootLevel(Level.OFF);
		}
		port = Integer.parseInt(properties.getProperty(SERVER_PORT_KEY));

		storeProperties();
		logger.traceExit("configure", null);
	}

	private void loadProperties() {
		logger.traceEntry("loadProperties");
		Path path = Paths.get(PROPERTIES_FILE);
		if (Files.exists(path)) {
			try (InputStream is = new FileInputStream(PROPERTIES_FILE)) {
				properties.load(is);
			} catch (FileNotFoundException e) {
				logger.error("File \"{}\" exists but not found! (Shouldn't get here normally)", PROPERTIES_FILE);
			} catch (IOException e) {
				logger.error("Problems with loading properties file \"{}\"", PROPERTIES_FILE);
			}
		} else {
			logger.warn("Properties file \"{}\" doesn't exist", PROPERTIES_FILE);
		}
		logger.traceExit("loadProperties", null);
	}

	private void storeProperties() {
		logger.traceEntry("storeProperties");
		// force store each key-value pair
		for (String key : properties.stringPropertyNames()) {
			properties.setProperty(key, properties.getProperty(key));
		}
		try (OutputStream os = new FileOutputStream(PROPERTIES_FILE)) {
			properties.store(os, PROPERTIES_COMMENT);
		} catch (IOException e) {
			logger.error("Problems with storing properties file \"{}\"", PROPERTIES_FILE);
		} finally {
			logger.traceExit("storeProperties", null);
		}
	}
}
