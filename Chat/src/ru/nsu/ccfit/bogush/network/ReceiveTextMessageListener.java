package ru.nsu.ccfit.bogush.network;

import ru.nsu.ccfit.bogush.message.types.TextMessage;

public interface ReceiveTextMessageListener {
	void receive(TextMessage msg);
}
