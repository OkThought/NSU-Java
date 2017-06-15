package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;

public class ServerTextMessage extends TextMessage implements Message {
	private final User author;

	public ServerTextMessage(TextMessage textMessage, User author) {
		super(textMessage);
		this.author = author;
	}

	public ServerTextMessage(String text, User author) {
		super(text);
		this.author = author;
	}

	public User getAuthor() {
		return author;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + author.getNickname() + ": \"" + getVerboseText() + "\")";
	}
}
