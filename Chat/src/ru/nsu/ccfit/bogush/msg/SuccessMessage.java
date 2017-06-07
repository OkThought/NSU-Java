package ru.nsu.ccfit.bogush.msg;

public class SuccessMessage implements Message {
	String content;

	public SuccessMessage(String content) {
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}
}
