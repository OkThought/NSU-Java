package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.message.types.Text;

public interface ReceiveTextMessageHandler {
	void receive(Text msg);
}
