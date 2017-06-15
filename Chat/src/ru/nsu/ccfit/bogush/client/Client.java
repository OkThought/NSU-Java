package ru.nsu.ccfit.bogush.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.*;
import ru.nsu.ccfit.bogush.client.view.handlers.ChatEventHandler;
import ru.nsu.ccfit.bogush.message.types.*;
import ru.nsu.ccfit.bogush.client.view.*;
import ru.nsu.ccfit.bogush.network.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements ChatEventHandler, LostConnectionListener, Runnable {
	private static final int READER_QUEUE_CAPACITY = 50;
	private static final int WRITER_QUEUE_CAPACITY = 50;

	static { LoggingConfiguration.addConfigFile(LoggingConfiguration.DEFAULT_LOGGER_CONFIG_FILE); }

	private static final Logger logger = LogManager.getLogger(Client.class.getSimpleName());

	private String host;
	private int port;
	private Socket socket;
	private SocketWriter socketWriter;
	private SocketReader socketReader;

	private Thread thread;

	private Session session;
	private LoginPayload loginPayload;

	private ArrayList<UserListChangeListener> userListChangeListeners = new ArrayList<>();
	private ArrayList<ReceiveTextMessageListener> receiveTextMessageListeners = new ArrayList<>();

	public static void main(String[] args) {
		logger.info("Launch client");
		Client client = new Client();
		SwingUtilities.invokeLater(client::prepareUI);
	}

	private void prepareUI() {
		ViewController viewController = new ViewController(this);
		viewController.addChatEventHandler(this);
		addUserListChangeListener(viewController);
		addReceiveTextMessageHandler(viewController);
	}

	private Client() {
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
		logger.info("Connecting to server on {}:{}", host, port);
		try {
			logger.trace("Opening socket");
			socket = new Socket(host, port);
			logger.trace("Opened socket on {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
			SocketMessageStream socketMessageStream = new SocketMessageStream(socket);
			socketReader = new SocketReader(socketMessageStream, this, READER_QUEUE_CAPACITY);
			socketWriter = new SocketWriter(socketMessageStream, WRITER_QUEUE_CAPACITY);
			start();
			logger.trace("Created {}", socketMessageStream.getClass().getSimpleName());
		} catch (IOException e) {
			logger.error("Couldn't open socket");
			return false;
		}
		logger.info("Connected successfully");
		return true;
	}

	@Override
	public boolean connect(String host, int port) {
		this.host = host;
		this.port = port;
		return connectToServer();
	}

	@Override
	public void disconnect() {
		logger.info("Disconnecting from the server");
		closeSocket();
		logger.info("Disconnected successfully");
	}

	@Override
	public void login(String nickname) {
		logger.info("Logging in with nickname \"{}\"", nickname);
		setLoginPayload(new LoginPayload(new User(nickname)));
		try {
			socketWriter.write(new LoginRequest(loginPayload));
		} catch (InterruptedException e) {
			logger.error("Couldn't send login message");
		}
	}

	@Override
	public void logout() {
		logger.info("Logging out");
		try {
			socketWriter.write(new LogoutRequest(session));
		} catch (InterruptedException e) {
			logger.error("Couldn't send logout message");
		}
	}

	@Override
	public void sendTextMessage(TextMessage msg) {
		ClientTextMessage textMessage = new ClientTextMessage(msg, session);
		logger.info("Sending {}", textMessage);
		try {
			socketWriter.write(textMessage);
		} catch (InterruptedException e) {
			logger.error("Couldn't send text message");
		}
	}

	@Override
	public void lostConnection() {
		logger.info("Lost connection with server");
		stop();
	}

	private void setLoginPayload(LoginPayload loginPayload) {
		logger.trace("Set login payload");
		this.loginPayload = loginPayload;
	}

	void onLoginSuccess(Session session) {
		this.session = session;
		try {
			socketWriter.write(new UserListRequest(session));
		} catch (InterruptedException e) {
			logger.error("Couldn't send user list request message");
		}
	}

	public User getUser() {
		return loginPayload.getUser();
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
		socketReader.stop();
		socketWriter.stop();
	}

	private void closeSocket() {
		try {
			logger.trace("Closing socket");
			socket.close();
		} catch (IOException e) {
			logger.fatal("Couldn't close socket");
			System.exit(1);
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
