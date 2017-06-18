package ru.nsu.ccfit.bogush.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.serialization.Serializer;
import ru.nsu.ccfit.bogush.message.Message;

public class MessageStream implements MessageSender, MessageReceiver {
	private static final Logger logger = LogManager.getLogger(MessageStream.class.getSimpleName());

	private Serializer<Message> serializer;

	public MessageStream(Serializer<Message> serializer) {
		this.serializer = serializer;
	}

	@Override
	public Message receiveMessage() throws MessageReceiver.Exception {
		try {
			Message msg = serializer.deserialize();
			logger.trace("Deserialized {}", msg);
			return msg;
		} catch (Serializer.SerializerException e) {
			throw new MessageReceiver.Exception(e);
		}
	}

	@Override
	public void sendMessage(Message message) throws MessageSender.Exception {
		try {
			logger.trace("Serializing {}...", message);
			serializer.serialize(message);
			logger.trace("Serialized successfully");
		} catch (Serializer.SerializerException e) {
			throw new MessageSender.Exception(e);
		}
	}
}
