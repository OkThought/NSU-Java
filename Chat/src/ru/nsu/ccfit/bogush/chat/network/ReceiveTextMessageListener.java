package ru.nsu.ccfit.bogush.chat.network;

import ru.nsu.ccfit.bogush.chat.User;
import ru.nsu.ccfit.bogush.chat.message.types.TextMessage;

public interface ReceiveTextMessageListener {
	void textMessageReceived(User author, TextMessage msg);
}
