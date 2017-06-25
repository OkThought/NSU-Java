package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageFactory;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "success")
@XmlType(factoryClass = MessageFactory.class, factoryMethod = "createEmptySuccess")
public class Success implements Message {
	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}
}
