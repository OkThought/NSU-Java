package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;

public class TextMessage implements Message {
	private String text;

	public TextMessage(TextMessage other) {
		this.text = other.text;
	}

	public TextMessage(String text) {
		this.text = text;
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
		return getClass().getSimpleName() + "(\"" + getVerboseText() + "\")";
	}
}
