package ru.nsu.ccfit.bogush.chat.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.chat.LoggingConfiguration;
import ru.nsu.ccfit.bogush.chat.User;
import ru.nsu.ccfit.bogush.chat.client.view.handlers.ChatEventHandler;
import ru.nsu.ccfit.bogush.chat.message.Message;
import ru.nsu.ccfit.bogush.chat.message.MessageFactory;
import ru.nsu.ccfit.bogush.chat.message.types.*;
import ru.nsu.ccfit.bogush.chat.client.view.*;
import ru.nsu.ccfit.bogush.chat.network.*;
import ru.nsu.ccfit.bogush.chat.serialization.MessageSerializerFactory;
import ru.nsu.ccfit.bogush.chat.serialization.Serializer;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

public class Client implements ChatEventHandler, LostConnectionListener, Runnable {
	static { LoggingConfiguration.setConfigFileToDefaultIfNotSet(); }
	private static final Logger logger = LogManager.getLogger(Client.class.getSimpleName());

	public static final String CHAT_VERSION = "Ivan's Chat";

	private static final Serializer.Type SERIALIZER_TYPE = Serializer.Type.XML_SERIALIZER;

	private static final int READER_QUEUE_CAPACITY = 50;

	private static final int WRITER_QUEUE_CAPACITY = 50;
	private static final String PROPERTIES_FILE                 = "client.properties";
	private static final String IP_KEY                          = "ip";
	private static final String PORT_KEY                        = "port";
	private static final String NICKNAME_KEY                    = "nickname";

	private static final String IP_DEFAULT                      = "0.0.0.0";
	private static final String PORT_DEFAULT                    = "0";
	private static final String NICKNAME_DEFAULT                = "nickname";

	private static final String PROPERTIES_COMMENT  = "Client properties file";

	private static final Properties DEFAULT_PROPERTIES = new Properties();

	static {
		DEFAULT_PROPERTIES.setProperty(IP_KEY, IP_DEFAULT);
		DEFAULT_PROPERTIES.setProperty(PORT_KEY, PORT_DEFAULT);
		DEFAULT_PROPERTIES.setProperty(NICKNAME_KEY, NICKNAME_DEFAULT);
	}

	private Properties properties = new Properties(DEFAULT_PROPERTIES);

	private ViewController viewController;

	private String host;
	private int port;
	private Socket socket;
	private SocketWriter socketWriter;
	private SocketReader socketReader;

	private Thread thread;

	private Session session;
	private User user;

	private ArrayList<UserListChangeListener> userListChangeListeners = new ArrayList<>();
	private ArrayList<ReceiveTextMessageListener> receiveTextMessageListeners = new ArrayList<>();

	public static void main(String[] args) {
		logger.info("Launch client");
		Client client = new Client();
		SwingUtilities.invokeLater(client::prepareUI);
	}

	private void prepareUI() {
		viewController = new ViewController(this,
				properties.getProperty(IP_KEY),
				properties.getProperty(PORT_KEY),
				properties.getProperty(NICKNAME_KEY));
		viewController.addChatEventHandler(this);
		addUserListChangeListener(viewController);
		addReceiveTextMessageHandler(viewController);
	}

	private Client() {
		configure();
		initThread();
	}

	private void initThread() {
		thread = new Thread(this, this.getClass().getSimpleName());
	}

	@Override
	public void run() {
		ClientMessageHandler clientMessageHandler = new ClientMessageHandler(this);
		while (!Thread.interrupted()) {
			try {
				socketReader.read().handleBy(clientMessageHandler);
			} catch (InterruptedException e) {
				logger.trace("Socket reader interrupted");
				break;
			}
		}
	}

	private boolean connectToServer() {
		logger.info("");
		logger.info("Connecting to server on {}:{}", host, port);
		try {
			logger.trace("Opening socket");
			socket = new Socket(host, port);
			logger.trace("Opened socket on {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			Serializer<Message> serializer = MessageSerializerFactory.createSerializer(in, out, SERIALIZER_TYPE);
			MessageStream messageStream = new MessageStream(serializer);
			logger.trace("Created {}", messageStream.getClass().getSimpleName());
			socketReader = new SocketReader(messageStream, this, READER_QUEUE_CAPACITY);
			socketWriter = new SocketWriter(messageStream, WRITER_QUEUE_CAPACITY);
			start();
		} catch (IOException e) {
			logger.error("Failed to open socket");
			return false;
		} catch (Serializer.SerializerException e) {
			logger.error("Failed to create serializer: {}", e.getMessage());
			return false;
		}
		logger.info("Connected successfully");
		logger.info("");
		return true;
	}

	@Override
	public boolean connect(String host, int port) {
		this.host = host;
		this.port = port;
		properties.setProperty(IP_KEY, host);
		properties.setProperty(PORT_KEY, String.valueOf(port));
		storeProperties();
		return connectToServer();
	}

	@Override
	public void disconnect() {
		logger.info("Disconnecting from the server");
		closeSocket();
		stop();
		logger.info("Disconnected successfully");
	}

	@Override
	public void login(String nickname) {
		logger.info("Logging in with nickname \"{}\"", nickname);
		setUser(new User(nickname));
		try {
			socketWriter.write(MessageFactory.createLoginRequest(nickname));
		} catch (InterruptedException e) {
			logger.error("Failed to send login message");
		}
	}

	@Override
	public void logout() {
		logger.info("Logging out");
		try {
			socketWriter.write(MessageFactory.createLogoutRequest(session));
		} catch (InterruptedException e) {
			logger.error("Failed to send logout message");
		}
	}

	@Override
	public void sendMessage(String msg) {
		TextMessageRequest textMessage = MessageFactory.createTextMessageRequest(msg, session);
		logger.info("Sending {}", textMessage);
		try {
			socketWriter.write(textMessage);
		} catch (InterruptedException e) {
			logger.error("Failed to send text message");
		}
	}

	@Override
	public void lostConnection() {
		logger.info("Lost connection with server");
		stop();
		viewController.lostConnection();
	}

	void onLoginSuccess(Session session) {
		setSession(session);
		try {
			socketWriter.write(MessageFactory.createUserListRequest(session));
		} catch (InterruptedException e) {
			logger.error("Failed to send user list request message");
		}
	}

	private void setSession(Session session) {
		logger.trace("Set session to {}", session);
		this.session = session;
	}

	public void setUser(User user) {
		this.user = user;
		properties.setProperty(NICKNAME_KEY, user.getName());
		storeProperties();
	}

	public User getUser() {
		return user;
	}

	private void start() {
		logger.trace("Start client");
		thread.start();
		socketReader.start();
		socketWriter.start();
	}

	private void stop() {
		logger.trace("Stop client");
		thread.interrupt();
		initThread();
		socketReader.stop();
		socketWriter.stop();
	}

	private void closeSocket() {
		try {
			logger.trace("Closing socket");
			socket.close();
		} catch (IOException e) {
			logger.fatal("Failed to close socket");
			System.exit(-1);
		}
	}

	private void configure() {
		logger.info("");
		logger.info("=== Configuration ===");
		loadProperties();
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
			logger.info("Properties file \"{}\" not found", PROPERTIES_FILE);
		}
	}

	private void storeProperties() {
		logger.info("Storing properties...");
		try (OutputStream os = new FileOutputStream(PROPERTIES_FILE)) {
			properties.store(os, PROPERTIES_COMMENT);
			logger.info("Saved properties in \"{}\" file successfully", PROPERTIES_FILE);
		} catch (IOException e) {
			logger.error("Problems with storing properties file \"{}\"", PROPERTIES_FILE);
		}
	}

	private void addUserListChangeListener(UserListChangeListener listener) {
		userListChangeListeners.add(listener);
	}

	private void addReceiveTextMessageHandler(ReceiveTextMessageListener handler) {
		receiveTextMessageListeners.add(handler);
	}

	ArrayList<UserListChangeListener> getUserListChangeListeners() {
		return userListChangeListeners;
	}

	ArrayList<ReceiveTextMessageListener> getReceiveTextMessageListeners() {
		return receiveTextMessageListeners;
	}
}
