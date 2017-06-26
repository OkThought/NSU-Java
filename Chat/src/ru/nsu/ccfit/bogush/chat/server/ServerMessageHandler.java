package ru.nsu.ccfit.bogush.chat.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.chat.message.DefaultMessageHandler;
import ru.nsu.ccfit.bogush.chat.message.types.*;
import ru.nsu.ccfit.bogush.chat.network.Session;

public class ServerMessageHandler extends DefaultMessageHandler {
	private static final Logger logger = LogManager.getLogger(ServerMessageHandler.class.getSimpleName());

	private ConnectedUser connectedUser;

	ServerMessageHandler(ConnectedUser connectedUser) {
		this.connectedUser = connectedUser;
	}

	@Override
	public void handle(LoginRequest message) {
		logger.trace("Handle {}", message);
		connectedUser.login(message.getUser());
	}

	@Override
	public void handle(LogoutRequest message) {
		logger.trace("Handle {}", message);
		logger.trace("Received logout request {} from {}", message, connectedUser.getUser());
		if (checkSession(message.getSession())) {
			connectedUser.logout();
		}
	}

	@Override
	public void handle(TextMessageRequest message) {
		logger.trace("Handle {}", message);
		if (checkSession(message.getSession())) {
			connectedUser.broadcastToOthers(new TextMessageEvent(message.getText(), connectedUser.getUser()));
			connectedUser.addToHistory(message);
		}
	}

	@Override
	public void handle(UserListRequest message) {
		logger.trace("Handle {}", message);
		if (checkSession(message.getSession())) {
			UserListSuccess userListSuccessMessage = new UserListSuccess(connectedUser.getUserList());
			try {
				connectedUser.sendMessage(userListSuccessMessage);
			} catch (InterruptedException e) {
				logger.error("Failed to send user-list to {}", connectedUser.getNickname());
			}
		}
	}

	private boolean checkSession(Session requested) {
		Session actual = connectedUser.getSession();
		if (!requested.equals(actual)) {
			logger.error("{} not equals actual {}", requested, actual);
			logger.warn("Probably '{}' wants to play hacker", connectedUser.getNickname());
			try {
				connectedUser.sendMessage(new ErrorMessage("Wrong session id"));
			} catch (InterruptedException e) {
				logger.error("Failed to send error message");
			}
			return false;
		}
		return true;
	}
}
