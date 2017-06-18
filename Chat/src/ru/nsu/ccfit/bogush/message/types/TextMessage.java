package ru.nsu.ccfit.bogush.message.types;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

public class TextMessage implements Serializable {
	@XmlElement(name = "message")
	private String text;

	public TextMessage() {
		text = "";
	}

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TextMessage that = (TextMessage) o;

		return text != null ? text.equals(that.text) : that.text == null;
	}

	@Override
	public int hashCode() {
		return text != null ? text.hashCode() : 0;
	}
}
