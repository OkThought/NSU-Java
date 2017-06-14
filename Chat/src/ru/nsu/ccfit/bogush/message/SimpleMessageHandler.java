package ru.nsu.ccfit.bogush.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.types.*;

public class SimpleMessageHandler implements MessageHandler {
	private static final Logger logger = LogManager.getLogger(SimpleMessageHandler.class.getSimpleName());

	@Override
	public void handle(Login message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(LoginSuccess message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(LoginError message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(UserList message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(UserEntered message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(Logout message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(LogoutSuccess message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(LogoutError message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(UserLeft message) {
		logger.trace("Handle {}", message);
		notSupported(message);
	}

	@Override
	public void handle(Text message) {
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
