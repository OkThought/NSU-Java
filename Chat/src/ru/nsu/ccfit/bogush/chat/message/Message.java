package ru.nsu.ccfit.bogush.chat.message;

import java.io.Serializable;

public interface Message extends Serializable {
	void handleBy(MessageHandler handler);
}
