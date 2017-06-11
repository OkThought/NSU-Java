package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.*;

public class ClientMessageHandler extends SimpleMessageHandler {
	private static final Logger logger = LogManager.getLogger();

	private Client client;

	public ClientMessageHandler(Client client) {
		this.client = client;
	}

	@Override
	public void handle(TextMessage message) {
		logger.info("[{}: {}]", message.getAuthor(), message.getText());
	}

	@Override
	public void handle(UserListMessage message) {
		logger.trace("Handle {}", message);
		User[] users = message.getUsers();
		for (UserListChangeListener listener : client.getUserListChangeListeners()) {
			listener.userListReceived(users);
		}
	}

	@Override
	public void handle(UserEnteredMessage message) {
		logger.trace("Handle {}", message);
		User user = message.getUser();
		for (UserListChangeListener listener : client.getUserListChangeListeners()) {
			listener.userEntered(user);
		}
	}

	@Override
	public void handle(UserLeftMessage message) {
		logger.trace("Handle {}", message);
		User user = message.getUser();
		for (UserListChangeListener listener : client.getUserListChangeListeners()) {
			listener.userLeft(user);
		}
	}
}
