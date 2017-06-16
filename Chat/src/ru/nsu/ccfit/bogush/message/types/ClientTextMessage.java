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
public class ClientTextMessage extends TextMessage implements Message, Request {
	@XmlAttribute(name = "name")
	private static final String COMMAND_NAME = "message";
	private final Session session;

	public ClientTextMessage() {
		session = new Session();
	}

	public ClientTextMessage(TextMessage textMessage, Session session) {
		super(textMessage);
		this.session = session;
	}

	public ClientTextMessage(String text, Session session) {
		super(text);
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
		return getClass().getSimpleName() + "(" + session + ": \"" + getVerboseText() + "\")";
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}
}
