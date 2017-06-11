package ru.nsu.ccfit.bogush.msg;

import java.io.Serializable;

public interface Message extends Serializable {
	void handleBy(MessageHandler handler);
}
