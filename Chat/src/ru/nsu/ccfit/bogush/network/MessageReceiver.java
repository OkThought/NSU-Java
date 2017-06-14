package ru.nsu.ccfit.bogush.network;

import ru.nsu.ccfit.bogush.message.Message;

public interface MessageReceiver {
	Message receiveMessage() throws Exception;
}
