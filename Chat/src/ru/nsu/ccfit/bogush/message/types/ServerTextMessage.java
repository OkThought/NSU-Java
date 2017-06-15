package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;

public class ServerTextMessage extends TextMessage {
	private final User author;

	public ServerTextMessage(TextMessage other, User author) {
		super(other);
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
	public String toString() {
		return getClass().getSimpleName() + "(" + author.getNickname() + ": \"" + getVerboseText() + "\")";
	}
}
