package ru.nsu.ccfit.bogush.message;

import java.io.Serializable;

public interface Message extends Serializable {
	void handleBy(MessageHandler handler);
}
