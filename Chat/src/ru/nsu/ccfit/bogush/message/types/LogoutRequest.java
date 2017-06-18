package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.message.MessageHandler;
import ru.nsu.ccfit.bogush.network.Session;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "command")
@XmlType
public class LogoutRequest implements Request {
	@XmlAttribute(name = "name")
	private static final String COMMAND_NAME = "logout";
	@XmlElement(name = "session")
	private Session session;

	public LogoutRequest() {
		session = new Session();
	}

	public LogoutRequest(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(\"" + session + "\")";
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LogoutRequest that = (LogoutRequest) o;

		return session != null ? session.equals(that.session) : that.session == null;
	}

	@Override
	public int hashCode() {
		return session != null ? session.hashCode() : 0;
	}
}
