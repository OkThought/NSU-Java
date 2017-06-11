package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.*;

public class SimpleMessageHandler implements MessageHandler {
	private static final Logger logger = LogManager.getLogger();

	@Override
	public void handle(SuccessMessage message) {
		logger.info("Success [{}]", message.getSuccessMessage());
	}

	@Override
	public void handle(ErrorMessage message) {
		logger.error("Error [{}]", message.getErrorMessage());
	}

	@Override
	public void handle(TextMessage message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(UserListMessage message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(UserEnteredMessage message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(UserLeftMessage message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(LoginMessage message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(LogoutMessage message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(Message message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	private void notSupported(Message message) {
		logger.error("This type of message ({}) is not supported!", message.getClass().getSimpleName());
	}
}
