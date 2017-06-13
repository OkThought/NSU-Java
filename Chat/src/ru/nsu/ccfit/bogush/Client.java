package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.types.Login;
import ru.nsu.ccfit.bogush.message.types.Logout;
import ru.nsu.ccfit.bogush.message.types.Text;
import ru.nsu.ccfit.bogush.message.types.UserList;
import ru.nsu.ccfit.bogush.view.*;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements ConnectHandler, LoginHandler, LogoutHandler, SendTextMessageHandler, Runnable {
	private static final int READER_QUEUE_CAPACITY = 50;
	private static final int WRITER_QUEUE_CAPACITY = 50;

	static { LoggingConfiguration.addConfigFile(LoggingConfiguration.DEFAULT_LOGGER_CONFIG_FILE); }

	private static final Logger logger = LogManager.getLogger(Client.class.getSimpleName());

	private String host;
	private int port;

	private Socket socket;

	private Thread thread;

	private User user;
	private LoginPayload loginPayload;
	private SocketMessageStream socketMessageStream;
	private SocketWriter socketWriter;
	private SocketReader socketReader;

	private ArrayList<UserListChangeListener> userListChangeListeners = new ArrayList<>();

	public static void main(String[] args) {
		logger.trace("Launch client");
		Client client = new Client();

		SwingUtilities.invokeLater(() -> {
			ViewController viewController = new ViewController(client);

			viewController.addConnectHandler(client);
			viewController.addLoginHandler(client);
			viewController.addLogoutHandler(client);
			viewController.addSendTextMessageHandler(client);
		});
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
				logger.error("Socket reader interrupted");
				break;
			}
		}
	}

	private boolean connectToServer() {
		logger.info("Connecting to server");
		try {
			logger.trace("Opening socket");
			socket = new Socket(host, port);
			logger.trace("Opened socket on {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
			socketMessageStream = new SocketMessageStream(socket);
			socketReader = new SocketReader(socketMessageStream, READER_QUEUE_CAPACITY);
			socketWriter = new SocketWriter(socketMessageStream, WRITER_QUEUE_CAPACITY);
			start();
			logger.trace("Created {}", socketMessageStream.getClass().getSimpleName());
		} catch (IOException e) {
			logger.error("Couldn't open socket");
			return false;
		}
		logger.info("Connected to {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
		return true;
	}

	@Override
	public boolean connect(String host, int port) {
		this.host = host;
		this.port = port;
		return connectToServer();
	}

	@Override
	public void login(LoginPayload loginPayload) {
		logger.info("Logging in with nickname \"{}\"", loginPayload.getNickname());
		setLoginPayload(loginPayload);
		setUser(new User(loginPayload));
		try {
			socketMessageStream.sendMessage(new Login(loginPayload));
		} catch (IOException e) {
			logger.error("Couldn't send login message");
		}

		try {
			socketMessageStream.sendMessage(new UserList());
		} catch (IOException e) {
			logger.error("Couldn't send user list request message");
		}
	}

	@Override
	public void logout() {
		logger.info("Disconnecting from {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
		try {
			socketMessageStream.sendMessage(new Logout(loginPayload));
			logger.info("Disconnected");
		} catch (IOException e) {
			logger.error("Couldn't send logout message");
		}
		closeSocket();
	}

	@Override
	public void sendTextMessage(String text) {
		logger.info("Sending text message \"{}\"", text.replaceAll("\\p{C}", "[]"));
		Text msg = new Text(user, text);
		try {
			socketMessageStream.sendMessage(msg);
		} catch (IOException e) {
			logger.error("Couldn't send text message");
		}
	}

	private void setLoginPayload(LoginPayload loginPayload) {
		logger.trace("Set login payload");
		this.loginPayload = loginPayload;

	}

	public void setUser(User user) {
		logger.trace("Set user to {}", user);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void start() {
		logger.trace("Start client");
		thread.start();
		socketReader.start();
		socketWriter.start();
	}

	public void stop() {
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

	public void addUserListChangeListener(UserListChangeListener listener) {
		userListChangeListeners.add(listener);
	}

	public ArrayList<UserListChangeListener> getUserListChangeListeners() {
		return userListChangeListeners;
	}
}