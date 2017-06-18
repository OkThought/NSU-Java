package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;
import ru.nsu.ccfit.bogush.network.Session;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "command")
@XmlType
public class UserListRequest implements Message, Request {
	@XmlAttribute(name = "name")
	private static final String COMMAND_NAME = "list";
	private final Session session;

	public UserListRequest() {
		session = new Session();
	}

	public UserListRequest(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	@XmlElement(name = "session")
	public void setSessionId(int id) {
		session.setId(id);
	}

	public int getSessionId() {
		return session.getId();
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + session + ")";
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserListRequest that = (UserListRequest) o;

		return session != null ? session.equals(that.session) : that.session == null;
	}

	@Override
	public int hashCode() {
		return session != null ? session.hashCode() : 0;
	}
}
