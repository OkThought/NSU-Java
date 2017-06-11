package ru.nsu.ccfit.bogush.msg;

public class ErrorMessage implements Message {
	private String errorMessage;

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

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + errorMessage + ")";
	}
}
