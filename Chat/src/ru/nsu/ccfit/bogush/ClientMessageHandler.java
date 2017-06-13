package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.types.Text;
import ru.nsu.ccfit.bogush.message.types.UserEntered;
import ru.nsu.ccfit.bogush.message.types.UserLeft;
import ru.nsu.ccfit.bogush.message.types.UserList;

public class ClientMessageHandler extends SimpleMessageHandler {
	private static final Logger logger = LogManager.getLogger(ClientMessageHandler.class.getSimpleName());

	private Client client;

	public ClientMessageHandler(Client client) {
		this.client = client;
	}

	@Override
	public void handle(Text message) {
		logger.info("[{}: \"{}\"]", message.getAuthor(), message.getVerboseText());
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
}
