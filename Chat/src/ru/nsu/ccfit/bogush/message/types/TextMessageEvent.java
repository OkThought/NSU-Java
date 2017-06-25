package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.MessageFactory;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "event")
@XmlType(factoryClass = MessageFactory.class, factoryMethod = "createEmptyTextMessageEvent")
public class TextMessageEvent extends TextMessage implements Event {
	private User author;

	public TextMessageEvent(String text, User author) {
		super(text);
		this.author = author;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	@XmlElement(name = "name")
	public String getAuthorName() {
		return getAuthor().getName();
	}

	public void setAuthorName(String name) {
		setAuthor(new User(name));
	}

	@XmlAttribute(name = "name")
	public String getEventName() {
		return "message";
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + author.getName() + ": \"" + getVerboseText() + "\")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		TextMessageEvent that = (TextMessageEvent) o;

		return (author != null ? author.equals(that.author) : that.author == null) && super.equals(o);
	}

	@Override
	public int hashCode() {
		return author != null ? author.hashCode() : 0;
	}
}
