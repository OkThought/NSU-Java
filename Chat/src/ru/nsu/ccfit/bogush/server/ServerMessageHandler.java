package ru.nsu.ccfit.bogush.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.SimpleMessageHandler;
import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.types.*;
import ru.nsu.ccfit.bogush.message.types.ErrorMessage;
import ru.nsu.ccfit.bogush.network.Session;

public class ServerMessageHandler extends SimpleMessageHandler {
	private static final Logger logger = LogManager.getLogger(ServerMessageHandler.class.getSimpleName());

	private ConnectedUser connectedUser;

	ServerMessageHandler(ConnectedUser connectedUser) {
		this.connectedUser = connectedUser;
	}

	@Override
	public void handle(LoginRequest message) {
		logger.trace("Handle {}", message);
		connectedUser.login(message.getLoginPayload());
	}

	@Override
	public void handle(LogoutRequest message) {
		logger.trace("Handle {}", message);
		logger.error("Received logout request {} from {}", message, connectedUser.getUser());
		if (checkSession(message.getSession())) {
			connectedUser.logout();
		}
	}

	@Override
	public void handle(ClientTextMessage message) {
		logger.trace("Handle {}", message);
		if (checkSession(message.getSession())) {
			connectedUser.broadcastToOthers(new ServerTextMessage(message, connectedUser.getUser()));
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
				logger.error("Couldn't send user-list to {}", connectedUser.getNickname());
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
				logger.error("Couldn't send error message");
			}
			return false;
		}
		return true;
	}
}
