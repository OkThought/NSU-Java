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

	ServerMessageHandler(Server server, ConnectedUser connectedUser) {
		this.server = server;
		this.connectedUser = connectedUser;
	}

	@Override
	public void handle(Login message) {
		logger.trace("Handle {}", message);
		user = connectedUser.login(message.getLoginPayload());
		nickname = connectedUser.getNickname();
	}

	@Override
	public void handle(Logout message) {
		logger.trace("Handle {}", message);
		String requested = message.getLoginPayload().getNickname();
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
			connectedUser.logout(user);
		}
	}

	@Override
	public void handle(Text message) {
		logger.trace("Handle {}", message);
		connectedUser.broadcastToOthers(message);
		server.addToHistory(message);
	}

	@Override
	public void handle(UserList message) {
		logger.trace("Handle {}", message);
		UserList userListMessage = new UserList(server.getUserList());
		try {
			connectedUser.sendMessage(userListMessage);
		} catch (InterruptedException e) {
			logger.error("Couldn't send user-list to {}", nickname);
		}
	}


}
