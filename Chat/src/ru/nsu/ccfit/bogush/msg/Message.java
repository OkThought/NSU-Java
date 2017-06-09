package ru.nsu.ccfit.bogush.msg;

public interface Message {
	void handleBy(MessageHandler handler);
}
