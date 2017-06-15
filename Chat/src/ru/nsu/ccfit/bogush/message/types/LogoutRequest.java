package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.network.LoginPayload;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;
import ru.nsu.ccfit.bogush.network.Session;

public class LogoutRequest implements Message {
	private Session session;

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
}
