package ru.nsu.ccfit.bogush.network;

import java.io.Serializable;

public class LoginPayload implements Serializable {
	private String nickname;

	public LoginPayload(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	@Override
	public String toString() {
		return getNickname();
	}
}
