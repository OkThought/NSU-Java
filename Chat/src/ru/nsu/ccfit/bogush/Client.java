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
	private SocketMessageStream socketMessageStream;
	private SocketWriter socketWriter;
	private SocketReader socketReader;

	public static void main(String[] args) {
		logger.trace("Start client");
		Client client = new Client();

		SwingUtilities.invokeLater(() -> {
			ViewController viewController = new ViewController();

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
		logger.trace("Client started");
	}

	private Client() {}

	private boolean connectToServer() {
		logger.info("Connecting to server");
		try {
			logger.trace("Opening socket");
			socket = new Socket(host, port);
			logger.trace("Opened socket on {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
			socketMessageStream = new SocketMessageStream(socket);
			logger.trace("Created {}", socketMessageStream.getClass().getSimpleName());
		} catch (IOException e) {
			logger.error("Couldn't open socket");
			return false;
		}
		logger.info("Connected to {}:{}", socket.getInetAddress().getHostName(), socket.getPort());
		return true;
	}

	public void setLoginPayload(LoginPayload loginPayload) {
		this.loginPayload = loginPayload;
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
