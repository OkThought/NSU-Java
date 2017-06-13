package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.*;
import ru.nsu.ccfit.bogush.message.types.*;

public class ServerMessageHandler extends SimpleMessageHandler {
	private static final Logger logger = LogManager.getLogger(ServerMessageHandler.class.getSimpleName());

	private Server server;
	private ConnectedUser connectedUser;
	private String nickname;

	public ServerMessageHandler(Server server, ConnectedUser connectedUser) {
		this.server = server;
		this.connectedUser = connectedUser;
	}

	@Override
	public void handle(Login message) {
		logger.trace("Handle {}", message);
		connectedUser.setLoginPayload(message.getLoginPayload());
		nickname = connectedUser.getNickname();

		try {
			connectedUser.sendMessage(new LoginSuccess("Logged in successfully"));
		} catch (InterruptedException e) {
			logger.error("Couldn't write success message");
		}
	}

	@Override
	public void handle(Logout message) {
		logger.trace("Handle {}", message);
		String requested = message.getLoginPayload().getNickname();
		logger.info("Received logout message from {}", requested);

		if (!requested.equals(nickname)) {
			logger.error("Nickname '{}' requested for logging out", requested);
			logger.error("is not the same as the actual nickname: '{}'", nickname);
			logger.warn("Probably '{}' wants to play hacker", nickname);
			try {
				connectedUser.sendMessage(new LoginError("Wrong nickname"));
			} catch (InterruptedException e) {
				logger.error("Couldn't send error message");
			}
		} else {
			server.logout(connectedUser);
			try {
				connectedUser.sendMessage(new LoginSuccess("Successfully logged out"));
			} catch (InterruptedException e) {
				logger.error("Couldn't send success message");
			}
			Message userLeft = new UserLeft(new User(nickname));
			for (ConnectedUser user : server.getConnectedUsers()) {
				try {
					user.sendMessage(userLeft);
				} catch (InterruptedException e) {
					logger.error("Couldn't send logout message to {}", user.getNickname());
				}
			}
		}
	}

	@Override
	public void handle(Text message) {
		logger.info("[{}: {}]", message.getAuthor(), message.getText());
		for (ConnectedUser user : server.getConnectedUsers()) {
			if (!user.getNickname().equals(message.getAuthor().getNickname())) {
				try {
					user.sendMessage(message);
				} catch (InterruptedException e) {
					logger.error("Couldn't send message to {}", user.toString());
				}
			} else {
				try {
					user.sendMessage(new LoginSuccess("Message received"));
				} catch (InterruptedException e) {
					logger.error("Couldn't send success message to {}", user.toString());
				}
			}
		}
		logger.info("Broadcast'ed message to all but sender");
		server.addToHistory(message);
	}

	@Override
	public void handle(UserList message) {
		logger.info("Received user-list request from {}", nickname);

		UserList userListMessage = new UserList(server.getUserList());
		try {
			connectedUser.sendMessage(userListMessage);
		} catch (InterruptedException e) {
			logger.error("Couldn't send user-list to {}", nickname);
			return;
		}
		logger.info("Sent user-list {} to {}", userListMessage.toString(), nickname);
	}
}
