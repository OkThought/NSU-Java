package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "event")
@XmlType
public class ServerTextMessage extends TextMessage implements Message, Event {
	@XmlAttribute(name = "name")
	private static final String EVENT_NAME = "message";
	private final User author;

	public ServerTextMessage() {
		super();
		author = new User();
	}

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

	@XmlElement(name = "name")
	public void setAuthorName(String name) {
		author.setNickname(name);
	}

	public String getAuthorName() {
		return author.getNickname();
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + author.getNickname() + ": \"" + getVerboseText() + "\")";
	}

	@Override
	public String getEventName() {
		return EVENT_NAME;
	}
}
