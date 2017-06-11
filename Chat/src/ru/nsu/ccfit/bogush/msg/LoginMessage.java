package ru.nsu.ccfit.bogush.msg;

import ru.nsu.ccfit.bogush.LoginPayload;

public class LoginMessage implements Message {
	private LoginPayload loginPayload;

	public LoginMessage(LoginPayload loginPayload) {
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
