package ru.nsu.ccfit.bogush;

import java.io.Serializable;

public class LoginPayload implements Serializable {
	private String nickname;

	public LoginPayload(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}
}
