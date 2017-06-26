package ru.nsu.ccfit.bogush.chat.server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import ru.nsu.ccfit.bogush.chat.LoggingConfiguration;
import ru.nsu.ccfit.bogush.chat.User;
import ru.nsu.ccfit.bogush.chat.message.Message;
import ru.nsu.ccfit.bogush.chat.message.types.*;
import ru.nsu.ccfit.bogush.chat.network.SocketAcceptor;
import ru.nsu.ccfit.bogush.chat.serialization.MessageSerializerFactory;
import ru.nsu.ccfit.bogush.chat.serialization.Serializer;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

public class Server {
	static { LoggingConfiguration.setConfigFileToDefaultIfNotSet(); }
	private static final Logger logger = LogManager.getLogger(Server.class.getSimpleName());

	private static final String DO_LOGGING_KEY = "log";
	private static final String SERVER_XML_PORT_KEY = "server-xml-port";
	private static final String SERVER_OBJ_PORT_KEY = "server-obj-port";
	private static final String HISTORY_CAPACITY_KEY = "message-history-capacity";
	private static final String IN_QUEUE_CAPACITY_KEY = "input-message-queue-capacity";
	private static final String OUT_QUEUE_CAPACITY_KEY = "output-message-queue-capacity";


	private static final String DO_LOGGING_DEFAULT = "true";
	private static final String SERVER_PORT_DEFAULT = "0";
	private static final String HISTORY_CAPACITY_DEFAULT = "50";
	private static final String IN_QUEUE_CAPACITY_DEFAULT = "50";
	private static final String OUT_QUEUE_CAPACITY_DEFAULT = "50";

	private static final String PROPERTIES_FILE = "server.properties";
	private static final String PROPERTIES_COMMENT = "Server properties file";
	private static final String STOP_COMMAND = "stop";
	private static final Properties DEFAULT_PROPERTIES = new Properties();

	static {
		DEFAULT_PROPERTIES.setProperty(DO_LOGGING_KEY, DO_LOGGING_DEFAULT);
		DEFAULT_PROPERTIES.setProperty(SERVER_XML_PORT_KEY, SERVER_PORT_DEFAULT);
		DEFAULT_PROPERTIES.setProperty(SERVER_OBJ_PORT_KEY, SERVER_PORT_DEFAULT);
		DEFAULT_PROPERTIES.setProperty(HISTORY_CAPACITY_KEY, HISTORY_CAPACITY_DEFAULT);
		DEFAULT_PROPERTIES.setProperty(IN_QUEUE_CAPACITY_KEY, IN_QUEUE_CAPACITY_DEFAULT);
		DEFAULT_PROPERTIES.setProperty(OUT_QUEUE_CAPACITY_KEY, OUT_QUEUE_CAPACITY_DEFAULT);
	}

	private Properties properties = new Properties(DEFAULT_PROPERTIES);

	private SocketAcceptor xmlAcceptor;
	private SocketAcceptor objAcceptor;
	private HashSet<ConnectedUser> connectedUsers = new HashSet<>();
	private ArrayBlockingQueue<TextMessageRequest> history;
	private int objPort;
	private int xmlPort;

	private static void help() {
		System.out.println("Type 'stop' to stop the server");
	}

	private static void displayLocalHostAddress() throws UnknownHostException {
		logger.info("Server localhost address: {}", InetAddress.getLocalHost().getHostAddress());
	}

	private static void enterConsoleMode(Server server) {
		logger.info("Entering console mode...\n\n");
		help();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			String command;
			while ((command = reader.readLine()) != null) {
				if (command.trim().toLowerCase().equals(STOP_COMMAND)) {
					logger.trace("Command '{}' received", STOP_COMMAND);
					server.stop();
					return;
				} else {
					System.out.println("Unknown command");
					help();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("eof found");
			server.stop();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		try {
			displayLocalHostAddress();
		} catch (UnknownHostException e) {
			logger.error("Failed to get localhost address");
		}
		Server server = new Server();
		server.start();
		enterConsoleMode(server);
	}

	private Server() {
		configure();
		objAcceptor = new SocketAcceptor(objPort,
				socket -> socketAccepted(socket, Serializer.Type.OBJ_SERIALIZER),
				"Object acceptor");
		xmlAcceptor = new SocketAcceptor(xmlPort,
				socket -> socketAccepted(socket, Serializer.Type.XML_SERIALIZER),
				"XML acceptor");
	}

	private void socketAccepted(Socket socket, Serializer.Type type) {
		try {
			logger.info("{} accepted", socket);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			logger.info("Creating serializer of type {}...", type);
			Serializer<Message> serializer = MessageSerializerFactory.createSerializer(in, out, type);
			logger.info("Serializer created successfully");
			ConnectedUser connectedUser = new ConnectedUser(this, socket, serializer);
			addConnectedUser(connectedUser);
		} catch (IOException e) {
			logger.error("Failed to get input or output socket stream");
		} catch (Serializer.SerializerException e) {
			logger.error("Failed to create serializer: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	private void addConnectedUser(ConnectedUser connectedUser) {
		if (connectedUsers.contains(connectedUser)) {
			logger.debug("User already connected");
		} else {
			connectedUsers.add(connectedUser);
			connectedUser.start();
		}
	}

	private void start() {
		logger.info("Start server");
		objAcceptor.start();
		xmlAcceptor.start();
		logger.info("Server started");
	}

	private void stop() {
		logger.info("Stopping the server");

		for (ConnectedUser user : connectedUsers) {
			user.stop();
		}
		objAcceptor.stop();
		xmlAcceptor.stop();

		logger.info("Server stopped. Exiting...");
	}

	void disconnect(ConnectedUser connectedUser) {
		connectedUsers.remove(connectedUser);
		connectedUser.stop();
	}

	void addToHistory(TextMessageRequest message) {
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
		logger.info("");
		logger.info("=== Configuration ===");
		loadProperties();

		boolean doLogging = Boolean.parseBoolean(properties.getProperty(DO_LOGGING_KEY));
		if (!doLogging) {
			Configurator.setRootLevel(Level.OFF);
		}

		xmlPort = Integer.parseInt(properties.getProperty(SERVER_XML_PORT_KEY));
		objPort = Integer.parseInt(properties.getProperty(SERVER_OBJ_PORT_KEY));

		int historyCapacity = Integer.parseInt(properties.getProperty(HISTORY_CAPACITY_KEY));
		history = new ArrayBlockingQueue<>(historyCapacity);

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
		for (Enumeration keys = properties.propertyNames(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			properties.setProperty(key, properties.getProperty(key));
		}

		try (OutputStream os = new FileOutputStream(PROPERTIES_FILE)) {
			properties.store(os, PROPERTIES_COMMENT);
		} catch (IOException e) {
			logger.error("Problems with storing properties file \"{}\"", PROPERTIES_FILE);
		}
	}
}
