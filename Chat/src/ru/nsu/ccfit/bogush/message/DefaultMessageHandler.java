package ru.nsu.ccfit.bogush.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.types.*;

public class DefaultMessageHandler implements MessageHandler {
	private static final Logger logger = LogManager.getLogger(DefaultMessageHandler.class.getSimpleName());

	@Override
	public void handle(LoginRequest message) {
		defaultHandle(message);
	}

	@Override
	public void handle(LoginEvent message) {
		defaultHandle(message);
	}

	@Override
	public void handle(LoginSuccess message) {
		defaultHandle(message);
	}

	@Override
	public void handle(UserListRequest message) {
		defaultHandle(message);
	}

	@Override
	public void handle(UserListSuccess message) {
		defaultHandle(message);
	}

	@Override
	public void handle(LogoutRequest message) {
		defaultHandle(message);
	}

	@Override
	public void handle(LogoutEvent message) {
		defaultHandle(message);
	}

	@Override
	public void handle(Success message) {
		defaultHandle(message);
	}

	@Override
	public void handle(TextMessageRequest message) {
		defaultHandle(message);
	}

	@Override
	public void handle(TextMessageEvent message) {
		defaultHandle(message);
	}

	@Override
	public void handle(ErrorMessage message) {
		defaultHandle(message);
	}

	@Override
	public void handle(Message message) {
		defaultHandle(message);
	}

	private void defaultHandle(Message message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	private void notSupported(Message message) {
		logger.error("This type of message ({}) is not supported!", message.getClass().getSimpleName());
	}
}
