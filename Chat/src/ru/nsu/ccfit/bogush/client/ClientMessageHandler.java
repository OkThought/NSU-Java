package ru.nsu.ccfit.bogush.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.network.ReceiveTextMessageListener;
import ru.nsu.ccfit.bogush.message.SimpleMessageHandler;
import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.types.*;

public class ClientMessageHandler extends SimpleMessageHandler {
	private static final Logger logger = LogManager.getLogger(ClientMessageHandler.class.getSimpleName());

	private Client client;

	public ClientMessageHandler(Client client) {
		this.client = client;
	}

	@Override
	public void handle(TextMessage message) {
		logger.trace("Handle {}", message);
		for (ReceiveTextMessageListener handler : client.getReceiveTextMessageListeners()) {
			handler.receive(message);
		}
	}

	@Override
	public void handle(UserList message) {
		logger.trace("Handle {}", message);
		User[] users = message.getUsers();
		for (UserListChangeListener listener : client.getUserListChangeListeners()) {
			listener.userListReceived(users);
		}
	}

	@Override
	public void handle(UserEntered message) {
		logger.trace("Handle {}", message);
		User user = message.getUser();
		for (UserListChangeListener listener : client.getUserListChangeListeners()) {
			listener.userEntered(user);
		}
	}

	@Override
	public void handle(UserLeft message) {
		logger.trace("Handle {}", message);
		User user = message.getUser();
		for (UserListChangeListener listener : client.getUserListChangeListeners()) {
			listener.userLeft(user);
		}
	}

	@Override
	public void handle(LoginSuccess message) {
		logger.trace("Handle {}", message);
		// TODO: login success
	}

	@Override
	public void handle(LoginError message) {
		logger.trace("Handle {}", message);
		// TODO: login error
	}

	@Override
	public void handle(LogoutSuccess message) {
		logger.trace("Handle {}", message);
		// TODO: logout success
	}

	@Override
	public void handle(LogoutError message) {
		logger.trace("Handle {}", message);
		// TODO: logout error
	}
}
