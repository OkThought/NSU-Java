package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class User {
	private static final Logger logger = LogManager.getLogger();

	private Server server;
	private Socket socket;
	private String name;

	public User(Server server, Socket socket) {
		logger.traceEntry("User");
		this.server = server;
		this.socket = socket;
		logger.traceExit("User", null);
	}
}
