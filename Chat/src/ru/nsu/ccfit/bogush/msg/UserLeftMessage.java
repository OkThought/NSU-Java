package ru.nsu.ccfit.bogush.msg;

import ru.nsu.ccfit.bogush.User;

public class UserLeftMessage implements Message {
	private User user;

	public UserLeftMessage(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(\"" + user + "\")";
	}
}
