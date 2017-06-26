package ru.nsu.ccfit.bogush.chat.serialization;

import org.junit.Test;
import ru.nsu.ccfit.bogush.chat.LoggingConfiguration;

public class MessageSerializerFactoryTest {
	static { LoggingConfiguration.setConfigFileToDefaultIfNotSet(); }
	@Test
	public void testInitJAXB() {
		MessageSerializerFactory.getJAXBContext();
	}
}
