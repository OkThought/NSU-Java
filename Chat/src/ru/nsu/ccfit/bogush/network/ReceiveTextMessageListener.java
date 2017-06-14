package ru.nsu.ccfit.bogush.network;

import ru.nsu.ccfit.bogush.message.types.Text;

public interface ReceiveTextMessageListener {
	void receive(Text msg);
}
