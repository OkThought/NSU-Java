package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.message.Message;

public interface MessageReceiver {
	Message receiveMessage() throws Exception;
}
