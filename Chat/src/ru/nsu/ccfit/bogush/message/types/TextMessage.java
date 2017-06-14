package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;

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

	public String getVerboseText() {
		return text.replaceAll("\\p{C}", "[]");
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return "[" +
				getAuthor() +
				": \"" +
				getVerboseText() +
				"\"]";
	}
}
