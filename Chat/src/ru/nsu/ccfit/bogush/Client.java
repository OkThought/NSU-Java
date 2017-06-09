package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.*;
import ru.nsu.ccfit.bogush.view.ViewController;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
	static { LoggingConfiguration.addConfigFile(LoggingConfiguration.DEFAULT_LOGGER_CONFIG_FILE); }

	private static final Logger logger = LogManager.getLogger();

	private String host;

	private int port;
	private Socket socket;

	private LoginPayload loginPayload;
	private User user;
	private SocketMessageStream socketMessageStream;

	public static void main(String[] args) {
		logger.trace("Start client");
		Client client = new Client();

		SwingUtilities.invokeLater(() -> {
			ViewController viewController = new ViewController();
			viewController.addLoginHandler((LoginPayload loginPayload) -> {

				client.setUser(new User(loginPayload.getNickname()));
				client.connectToServer();
			});
		});
		logger.trace("Client started");
	}

	private Client() {}

	public void setLoginPayload(LoginPayload loginPayload) {
		this.loginPayload = loginPayload;
	}

	private void setUser(User user) {
		this.user = user;
	}

	private void connectToServer() {
		logger.info("Connecting to server");
		try {
			logger.trace("Opening socket");
			socket = new Socket(host, port);
			socketMessageStream = new SocketMessageStream(socket);
		} catch (IOException e) {
			logger.fatal("Couldn't open socket");
			closeSocket();
			System.exit(1);
		}

		logger.trace("Opened socket on {}:{}", socket.getInetAddress().getHostName(), socket.getPort());

		try {
			socketMessageStream.sendMessage(new LoginMessage(loginPayload));
		} catch (IOException e) {
			logger.error("Couldn't send message");
		}

		logger.info("Connected to {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
	}

	public void disconnectFromServer() {
		logger.info("Disconnecting from {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
		try {
			socketMessageStream.sendMessage(new LogoutMessage(loginPayload));
			logger.info("Disconnected");
		} catch (IOException e) {
			logger.error("Couldn't send LogoutMessage");
		}
		closeSocket();
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
}
