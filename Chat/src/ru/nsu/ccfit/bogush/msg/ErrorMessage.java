package ru.nsu.ccfit.bogush.msg;

public class ErrorMessage implements Message {
	String content;

	public ErrorMessage(String content) {
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}
}
