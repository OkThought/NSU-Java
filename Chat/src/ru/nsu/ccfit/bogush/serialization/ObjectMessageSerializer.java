package ru.nsu.ccfit.bogush.serialization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.Message;

import java.io.*;

public class ObjectMessageSerializer implements Serializer<Message> {
	private static final Logger logger = LogManager.getLogger(ObjectMessageSerializer.class.getSimpleName());

	private ObjectInputStream in;
	private ObjectOutputStream out;

	ObjectMessageSerializer(InputStream in, OutputStream out) throws SerializerException {
		try {
			this.out = new ObjectOutputStream(out);
			this.out.flush();
			this.in = new ObjectInputStream(in);
		} catch (IOException e) {
			throw new SerializerException(e);
		}
	}

	@Override
	public void serialize(Message obj) throws SerializerException {
		try {
			out.writeObject(obj);
			out.flush();
		} catch (IOException e) {
			throw new SerializerException(e);
		}
	}

	@Override
	public Message deserialize() throws SerializerException {
		try {
			return (Message) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new SerializerException(e);
		}
	}

	@Override
	public Type getType() {
		return Type.OBJ_SERIALIZER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
