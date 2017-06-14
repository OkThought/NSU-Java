package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.*;
import ru.nsu.ccfit.bogush.message.types.*;

public class ServerMessageHandler extends SimpleMessageHandler {
	private static final Logger logger = LogManager.getLogger(ServerMessageHandler.class.getSimpleName());

	private Server server;
	private ConnectedUser connectedUser;
	private User user;
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
		user = new User(nickname);
		broadcastToOthers(new UserEntered(user));
		logger.info("Sending login success message back to {}", user);
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
				connectedUser.sendMessage(new LogoutError("Wrong nickname"));
			} catch (InterruptedException e) {
				logger.error("Couldn't send error message");
			}
		} else {
			server.logout(connectedUser);
			try {
				connectedUser.sendMessage(new LogoutSuccess("Successfully logged out"));
			} catch (InterruptedException e) {
				logger.error("Couldn't send success message");
			}
			broadcastToOthers(new UserLeft(user));
		}
	}

	@Override
	public void handle(Text message) {
		logger.trace("Handle {}", message);
		broadcastToOthers(message);
		server.addToHistory(message);
	}

	@Override
	public void handle(UserList message) {
		logger.info("Received {} from \"{}\"", message, nickname);

		UserList userListMessage = new UserList(server.getUserList());
		try {
			connectedUser.sendMessage(userListMessage);
		} catch (InterruptedException e) {
			logger.error("Couldn't send user-list to {}", nickname);
		}
	}

	private void broadcastToOthers(Message msg) {
		logger.info("Broadcasting to others {}", msg);
		for (ConnectedUser cu : server.getConnectedUsers()) {
			if (!cu.equals(connectedUser)) {
				try {
					cu.sendMessage(msg);
				} catch (InterruptedException e) {
					logger.error("Couldn't send {} to {}", msg, cu);
				}
			}
		}
	}
}
