package ru.nsu.ccfit.bogush.client.view;

import ru.nsu.ccfit.bogush.User;

public class TextMessage {
	private User author;
	private String text;

	public TextMessage(User author, String text) {
		this.author = author;
		this.text = text;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "TextMessage(" + author + ": \"" +
				text.replaceAll("\\p{C}", "[]") + "\")";
	}
}
