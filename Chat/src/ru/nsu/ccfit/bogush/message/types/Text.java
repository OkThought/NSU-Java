package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;

public class Text implements Message {
	private User author;
	private String text;

	public Text(User author, String text) {
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
