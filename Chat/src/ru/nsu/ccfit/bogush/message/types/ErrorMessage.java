package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageFactory;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "error")
@XmlType(factoryClass = MessageFactory.class, factoryMethod = "createEmptyErrorMessage")
public class ErrorMessage implements Message {
	private String message;

	public ErrorMessage(String message) {
		this.message = message;
	}

	@XmlElement(name = "message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(\"" + message + "\")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ErrorMessage that = (ErrorMessage) o;

		return message != null ? message.equals(that.message) : that.message == null;
	}

	@Override
	public int hashCode() {
		return message != null ? message.hashCode() : 0;
	}
}
