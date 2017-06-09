package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.msg.Message;

import java.io.IOException;

public interface MessageSender {
	void sendMessage(Message message) throws IOException;
}
