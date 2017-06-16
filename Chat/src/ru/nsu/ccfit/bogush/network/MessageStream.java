package ru.nsu.ccfit.bogush.network;

import ru.nsu.ccfit.bogush.serialization.Serializer;
import ru.nsu.ccfit.bogush.message.Message;

public class MessageStream implements MessageSender, MessageReceiver {
	private Serializer<Message> serializer;

	public MessageStream(Serializer<Message> serializer) {
		this.serializer = serializer;
	}

	@Override
	public Message receiveMessage() throws MessageReceiver.Exception {
		try {
			return serializer.deserialize();
		} catch (Serializer.SerializerException e) {
			throw new MessageReceiver.Exception(e);
		}
	}

	@Override
	public void sendMessage(Message message) throws MessageSender.Exception {
		try {
			serializer.serialize(message);
		} catch (Serializer.SerializerException e) {
			throw new MessageSender.Exception(e);
		}
	}
}
