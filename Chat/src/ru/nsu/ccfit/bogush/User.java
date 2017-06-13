package ru.nsu.ccfit.bogush;

import java.io.Serializable;

public class User implements Serializable {
	private String nickname;

	public User(String nickname) {
		this.nickname = nickname;
	}

	public User(LoginPayload loginPayload) {
		nickname = loginPayload.getNickname();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return getNickname();
	}

	@Override
	public int hashCode() {
		return nickname != null ? nickname.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof User && ((User) obj).getNickname().equals(nickname);
	}
}
