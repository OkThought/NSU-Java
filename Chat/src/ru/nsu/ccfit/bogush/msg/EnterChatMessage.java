package ru.nsu.ccfit.bogush.msg;

public class EnterChatMessage implements Message {
	String nick;

	public EnterChatMessage(String nick) {
		this.nick = nick;
	}

	@Override
	public String getContent() {
		return nick;
	}
}
