package ru.nsu.ccfit.bogush.msg;

public class ErrorMessage implements Message {
	String errorMessage;

	public ErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}
}
