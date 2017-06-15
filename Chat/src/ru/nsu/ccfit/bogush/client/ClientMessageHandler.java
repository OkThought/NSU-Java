package ru.nsu.ccfit.bogush.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.types.ErrorMessage;
import ru.nsu.ccfit.bogush.network.ReceiveTextMessageListener;
import ru.nsu.ccfit.bogush.message.DefaultMessageHandler;
import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.types.*;

public class ClientMessageHandler extends DefaultMessageHandler {
	private static final Logger logger = LogManager.getLogger(ClientMessageHandler.class.getSimpleName());

	private Client client;

	public ClientMessageHandler(Client client) {
		this.client = client;
	}

	@Override
	public void handle(LoginEvent message) {
		logger.trace("Handle {}", message);
		User user = message.getUser();
		for (UserListChangeListener listener : client.getUserListChangeListeners()) {
			listener.userEntered(user);
		}
	}

	@Override
	public void handle(LoginSuccess message) {
		logger.trace("Handle {}", message);
		logger.info("Received {}", message);
		client.onLoginSuccess(message.getSession());
	}

	@Override
	public void handle(LogoutEvent message) {
		logger.trace("Handle {}", message);
		User user = message.getUser();
		for (UserListChangeListener listener : client.getUserListChangeListeners()) {
			listener.userLeft(user);
		}
	}

	@Override
	public void handle(LogoutSuccess message) {
		logger.trace("Handle {}", message);
		logger.info("Logged out successfully");
	}

	@Override
	public void handle(UserListSuccess message) {
		logger.trace("Handle {}", message);
		User[] users = message.getUsers();
		for (UserListChangeListener listener : client.getUserListChangeListeners()) {
			listener.userListReceived(users);
		}
	}

	@Override
	public void handle(ServerTextMessage message) {
		logger.trace("Handle {}", message);
		for (ReceiveTextMessageListener handler : client.getReceiveTextMessageListeners()) {
			handler.textMessageReceived(message.getAuthor(), message);
		}
	}

	@Override
	public void handle(ErrorMessage message) {
		logger.trace("Handle {}", message);
		logger.error(message.getMessage());
	}
}
