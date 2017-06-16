package ru.nsu.ccfit.bogush.serialization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.types.*;
import ru.nsu.ccfit.bogush.server.Server;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class MessageSerializerFactory {
	private static final Logger logger = LogManager.getLogger(Server.class.getSimpleName());
	private static JAXBContext JAXB_CONTEXT = null;

	private static void jaxbInit() {
		try {
			logger.trace("Configuring JAXB..");
			JAXB_CONTEXT = JAXBContext.newInstance(
					LoginRequest.class,
					LoginEvent.class,
					LoginSuccess.class,
					LogoutRequest.class,
					LogoutEvent.class,
					LogoutSuccess.class,
					UserListRequest.class,
					UserListSuccess.class,
					ServerTextMessage.class,
					ClientTextMessage.class,
					ErrorMessage.class);
			logger.trace("Configured finely");
		} catch (JAXBException e) {
			logger.fatal("Failed to configure: {}", e.getMessage());
			System.exit(1);
		}
	}

	public static JAXBContext getJAXBContext() {
		if (JAXB_CONTEXT == null) {
			jaxbInit();
		}
		return JAXB_CONTEXT;
	}

	public static Serializer<Message>
	createSerializer(InputStream in, OutputStream out,
	                 Serializer.Type type) throws Serializer.SerializerException {
		logger.trace("Creating serializer of type {}", type);
		switch (type.getType()) {
			case Serializer.Type.OBJ:
				logger.trace("Creating ObjectMessageSerializer");
				return new ObjectMessageSerializer(in, out);
			case Serializer.Type.XML:
				logger.trace("Creating XMLMessageSerializer");
				return new XMLMessageSerializer(in, out, getJAXBContext());
			default:
				logger.error("This type of serializer ({}) is not supported!", type);
				return null;
		}
	}
}
