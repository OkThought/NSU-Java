package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.*;
import ru.nsu.ccfit.bogush.view.ViewController;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
	private static final int READER_QUEUE_CAPACITY = 50;
	private static final int WRITER_QUEUE_CAPACITY = 50;

	static { LoggingConfiguration.addConfigFile(LoggingConfiguration.DEFAULT_LOGGER_CONFIG_FILE); }

	private static final Logger logger = LogManager.getLogger();

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

			viewController.addConnectHandler((host, port) -> {
				client.host = host;
				client.port = port;
				return client.connectToServer();
			});

			viewController.addLoginHandler((LoginPayload loginPayload) -> {
				client.setLoginPayload(loginPayload);
				client.login();
			});

		});
	}

	private Client() {
		thread = new Thread(() -> {
			ClientMessageHandler clientMessageHandler = new ClientMessageHandler(this);
			while (!Thread.interrupted()) {
				try {
					socketReader.read().handleBy(clientMessageHandler);
				} catch (InterruptedException e) {
					logger.error("Socket reader interrupted");
					break;
				}
			}
		});
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

	private void setLoginPayload(LoginPayload loginPayload) {
		this.loginPayload = loginPayload;
		user = new User(loginPayload);
	}

	public User getUser() {
		return user;
	}

	private void login() {
		logger.info("Logging in with nickname \"{}\"", loginPayload.getNickname());
		try {
			socketMessageStream.sendMessage(new LoginMessage(loginPayload));
		} catch (IOException e) {
			logger.error("Couldn't send login message");
		}
	}

	public void disconnectFromServer() {
		logger.info("Disconnecting from {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
		try {
			socketMessageStream.sendMessage(new LogoutMessage(loginPayload));
			logger.info("Disconnected");
		} catch (IOException e) {
			logger.error("Couldn't send logout message");
		}
		closeSocket();
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
