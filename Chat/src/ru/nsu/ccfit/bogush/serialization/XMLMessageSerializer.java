package ru.nsu.ccfit.bogush.serialization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.server.Server;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;

public class XMLMessageSerializer implements Serializer<Message> {
	private static final Logger logger = LogManager.getLogger(Server.class.getSimpleName());
	private InputStream in;
	private OutputStream out;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	XMLMessageSerializer(InputStream in, OutputStream out, JAXBContext jaxbContext) throws SerializerException {
		this.in = in;
		this.out = out;
		try {
			marshaller = jaxbContext.createMarshaller();
			unmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new SerializerException(e);
		}
	}

	@Override
	public void serialize(Message obj) throws SerializerException {
		logger.trace("Serializing {}", obj);
		try {
			marshaller.marshal(obj, out);
			logger.trace("Serialized successfully");
		} catch (JAXBException e) {
			throw new SerializerException(e);
		}
	}

	@Override
	public Message deserialize() throws SerializerException {
		logger.trace("Deserializing");
		try {
			Message message = (Message) unmarshaller.unmarshal(in);
			logger.trace("Deserialized {} successfully", message);
			return message;
		} catch (JAXBException e) {
			throw new SerializerException(e);
		}
	}

	@Override
	public Type getType() {
		return Type.XML_SERIALIZER;
	}
}
