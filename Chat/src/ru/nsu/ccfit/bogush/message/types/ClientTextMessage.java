package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;
import ru.nsu.ccfit.bogush.network.Session;

public class ClientTextMessage extends TextMessage implements Message {
	private final Session session;

	public ClientTextMessage(TextMessage textMessage, Session session) {
		super(textMessage);
		this.session = session;
	}

	public ClientTextMessage(String text, Session session) {
		super(text);
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + session + ": \"" + getVerboseText() + "\")";
	}
}
