package ru.nsu.ccfit.bogush.network;

import ru.nsu.ccfit.bogush.User;

import java.io.Serializable;

public class LoginPayload implements Serializable {
	public static final String OBJ = "obj";
	public static final String XML = "xml";
	private User user;
	private String type;

	public LoginPayload(User user) {
		this(user, OBJ);
	}

	public LoginPayload(User user, String type) {
		this.user = user;
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(type: \"" + type + "\", nickname: \"" + user.getNickname() + "\")";
	}
}
