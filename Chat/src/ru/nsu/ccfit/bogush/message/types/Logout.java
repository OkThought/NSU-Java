package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.LoginPayload;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;

public class Logout implements Message {
	private LoginPayload loginPayload;

	public Logout(LoginPayload loginPayload) {
		this.loginPayload = loginPayload;
	}

	public LoginPayload getLoginPayload() {
		return loginPayload;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(\"" + loginPayload.getNickname() + "\")";
	}
}
