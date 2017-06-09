package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.msg.Message;

public interface MessageReceiver {
	Message receiveMessage() throws Exception;
}
