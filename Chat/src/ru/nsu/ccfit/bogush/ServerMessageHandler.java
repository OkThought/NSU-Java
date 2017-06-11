package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.*;

public class ServerMessageHandler implements MessageHandler {
	private static final Logger logger = LogManager.getLogger();

	private Server server;
	private ConnectedUser connectedUser;
	private String nickname;

	public ServerMessageHandler(Server server, ConnectedUser connectedUser) {
		this.server = server;
		this.connectedUser = connectedUser;
	}

	@Override
	public void handle(LoginMessage message) {
		logger.trace("Handle {}", message);
		connectedUser.setLoginPayload(message.getLoginPayload());
		nickname = connectedUser.getNickname();
		try {
			connectedUser.sendMessage(new SuccessMessage("Logged in successfully"));
		} catch (InterruptedException e) {
			logger.error("Couldn't write success message");
		}
	}

	@Override
	public void handle(LogoutMessage message) {
		String requested = message.getLoginPayload().getNickname();
		logger.info("Received logout message from {}", requested);

		if (!requested.equals(nickname)) {
			logger.error("Nickname '{}' requested for logging out", requested);
			logger.error("is not the same as the actual nickname: '{}'", nickname);
			logger.warn("Probably '{}' wants to play hacker", nickname);
			try {
				connectedUser.sendMessage(new ErrorMessage("Wrong nickname"));
			} catch (InterruptedException e) {
				logger.error("Couldn't send error message");
			}
		} else {
			server.logout(connectedUser);
			try {
				connectedUser.sendMessage(new SuccessMessage("Successfully logged out"));
			} catch (InterruptedException e) {
				logger.error("Couldn't send success message");
			}
			for (ConnectedUser user : server.getConnectedUsers()) {
				try {
					user.sendMessage(message);
				} catch (InterruptedException e) {
					logger.error("Couldn't send logout message to {}", user.getNickname());
				}
			}
		}
	}

	@Override
	public void handle(SuccessMessage message) {
		logger.info("Success [{}]", message.getSuccessMessage());
	}

	@Override
	public void handle(ErrorMessage message) {
		logger.info("Error [{}]", message.getErrorMessage());
	}

	@Override
	public void handle(TextMessage message) {
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
					user.sendMessage(new SuccessMessage("Message received"));
				} catch (InterruptedException e) {
					logger.error("Couldn't send success message to {}", user.toString());
				}
			}
		}
		logger.info("Broadcast'ed message to all but sender");
		server.addToHistory(message);
	}

	@Override
	public void handle(UserListMessage message) {
		logger.info("Received user-list request from {}", nickname);

		UserListMessage userListMessage = new UserListMessage(server.getUserList());
		try {
			connectedUser.sendMessage(userListMessage);
		} catch (InterruptedException e) {
			logger.error("Couldn't send user-list to {}", nickname);
			return;
		}
		logger.info("Sent user-list {} to {}", userListMessage.toString(), nickname);
	}

	@Override
	public void handle(Message message) {

	}
}
