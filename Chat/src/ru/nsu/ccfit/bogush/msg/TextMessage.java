package ru.nsu.ccfit.bogush.msg;

import ru.nsu.ccfit.bogush.User;

public class TextMessage implements Message {
	private User author;
	private String text;

	public TextMessage(User author, String text) {
		this.author = author;
		this.text = text;
	}

	public User getAuthor() {
		return author;
	}

	public String getText() {
		return text;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("[")
				.append(getAuthor())
				.append(": \"")
				.append(getText())
				.append("\"]")
				.toString();
	}
}
