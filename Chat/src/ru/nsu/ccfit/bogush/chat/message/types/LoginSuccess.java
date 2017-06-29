package ru.nsu.ccfit.bogush.chat.message.types;

import ru.nsu.ccfit.bogush.chat.message.MessageFactory;
import ru.nsu.ccfit.bogush.chat.message.MessageHandler;
import ru.nsu.ccfit.bogush.chat.network.Session;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "success")
@XmlType(factoryClass = MessageFactory.class, factoryMethod = "createEmptyLoginSuccess")
@XmlAccessorType(XmlAccessType.NONE)
public class LoginSuccess extends Success {
	private Session session;

	public LoginSuccess(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@XmlElement(name = "session")
	public int getSessionId() {
		return getSession().getId();
	}

	public void setSessionId(int sessionId) {
		if (session == null) {
			setSession(new Session(sessionId));
		} else {
			session.setId(sessionId);
		}
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + session +")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		LoginSuccess that = (LoginSuccess) o;

		return session != null ? session.equals(that.session) : that.session == null;
	}

	@Override
	public int hashCode() {
		return session != null ? session.hashCode() : 0;
	}
}
