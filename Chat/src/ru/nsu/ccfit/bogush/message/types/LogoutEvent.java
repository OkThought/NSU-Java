package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "event")
@XmlType
public class LogoutEvent implements Event {
	@XmlAttribute(name = "name")
	private static final String EVENT_NAME = "userlogout";
	private User user;

	public LogoutEvent() {}

	public LogoutEvent(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(\"" + user + "\")";
	}

	@Override
	public String getEventName() {
		return EVENT_NAME;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LogoutEvent that = (LogoutEvent) o;

		return user != null ? user.equals(that.user) : that.user == null;
	}

	@Override
	public int hashCode() {
		return user != null ? user.hashCode() : 0;
	}
}
