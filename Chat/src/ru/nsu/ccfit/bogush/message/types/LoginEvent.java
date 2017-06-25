package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.MessageFactory;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "event")
@XmlType(factoryClass = MessageFactory.class, factoryMethod = "createEmptyLoginEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class LoginEvent implements Event {
	private User user;

	public LoginEvent(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@XmlElement(name = "name")
	public String getUserName() {
		return getUser().getName();
	}

	public void setUserName(String name) {
		if (user == null) {
			setUser(new User(name));
		} else {
			user.setName(name);
		}
	}

	@XmlAttribute(name = "name")
	public String getEventName() {
		return "userlogin";
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LoginEvent that = (LoginEvent) o;

		return user != null ? user.equals(that.user) : that.user == null;
	}

	@Override
	public int hashCode() {
		return user != null ? user.hashCode() : 0;
	}
}
