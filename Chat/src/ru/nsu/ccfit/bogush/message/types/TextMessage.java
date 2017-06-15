package ru.nsu.ccfit.bogush.message.types;

import java.io.Serializable;

public class TextMessage implements Serializable {
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
	public String toString() {
		return getClass().getSimpleName() + "(\"" + getVerboseText() + "\")";
	}
}
