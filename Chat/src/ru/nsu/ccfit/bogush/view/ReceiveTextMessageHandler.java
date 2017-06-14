package ru.nsu.ccfit.bogush.view;

import ru.nsu.ccfit.bogush.message.types.Text;

public interface ReceiveTextMessageHandler {
	void receive(Text msg);
}
