package ru.nsu.ccfit.bogush.client.view;

import ru.nsu.ccfit.bogush.message.types.TextMessage;

public interface SendTextMessageHandler {
	void sendTextMessage(TextMessage textMessage);
}
