package ru.nsu.ccfit.bogush.chat.message.types;

import ru.nsu.ccfit.bogush.chat.User;
import ru.nsu.ccfit.bogush.chat.message.MessageFactory;
import ru.nsu.ccfit.bogush.chat.message.MessageHandler;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "command")
@XmlType(factoryClass = MessageFactory.class, factoryMethod = "createEmptyLoginRequest")
@XmlAccessorType(XmlAccessType.NONE)
public class LoginRequest implements Request {
	private User user;

	public LoginRequest(User user, String type) {
		this.user = user;
		user.setType(type);
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

	@XmlElement(name = "type")
	public String getType() {
		return user.getType();
	}

	public void setType(String type) {
		user.setType(type);
	}

	@XmlAttribute(name = "name")
	public String getRequestName() {
		return "login";
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + user + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LoginRequest that = (LoginRequest) o;

		return user != null ? user.equals(that.user) : that.user == null;
	}

	@Override
	public int hashCode() {
		return user != null ? user.hashCode() : 0;
	}
}
