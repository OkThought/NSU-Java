package ru.nsu.ccfit.bogush.msg.handler;
import ru.nsu.ccfit.bogush.msg.Message;

public interface MessageHandler {
	void processMessage(Message message);
}
