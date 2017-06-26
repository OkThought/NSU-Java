package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.message.MessageFactory;
import ru.nsu.ccfit.bogush.message.MessageHandler;
import ru.nsu.ccfit.bogush.network.Session;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="command")
@XmlType(factoryClass = MessageFactory.class, factoryMethod = "createEmptyTextMessageRequest")
@XmlAccessorType(XmlAccessType.NONE)
public class TextMessageRequest extends TextMessage implements Request {
	private Session session;

	public TextMessageRequest(String text, Session session) {
		super(text);
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

	@XmlAttribute(name = "name")
	public String getRequestName() {
		return "message";
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + session + ": \"" + getVerboseText() + "\")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TextMessageRequest that = (TextMessageRequest) o;

		return (session != null ? session.equals(that.session) : that.session == null) && super.equals(o);
	}

	@Override
	public int hashCode() {
		return session != null ? session.hashCode() : 0;
	}
}
