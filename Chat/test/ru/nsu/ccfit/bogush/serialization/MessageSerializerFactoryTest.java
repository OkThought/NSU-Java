package ru.nsu.ccfit.bogush.serialization;

import org.junit.Test;
import ru.nsu.ccfit.bogush.LoggingConfiguration;

public class MessageSerializerFactoryTest {
	static { LoggingConfiguration.setConfigFile(); }
	@Test
	public void testInitJAXB() {
		MessageSerializerFactory.getJAXBContext();
	}
}