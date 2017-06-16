package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "success")
@XmlType
public class LogoutSuccess implements Message {
	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
