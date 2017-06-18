package ru.nsu.ccfit.bogush.network;

import ru.nsu.ccfit.bogush.User;

import java.io.Serializable;

public class LoginPayload implements Serializable {
	private User user;

	public LoginPayload() { this(new User()); }

	public LoginPayload(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(type: \"" + user.getType() + "\", nickname: \"" + user.getNickname() + "\")";
	}
}
