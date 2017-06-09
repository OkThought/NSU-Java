package ru.nsu.ccfit.bogush;

public class LoginPayload {
	private String nickname;

	public LoginPayload(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}
}
