package ru.nsu.ccfit.bogush.msg;

public class SuccessMessage implements Message {
	String successMessage;

	public SuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + successMessage + ")";
	}
}
