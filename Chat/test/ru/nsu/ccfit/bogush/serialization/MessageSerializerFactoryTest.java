package ru.nsu.ccfit.bogush.serialization;

import org.junit.Test;

public class MessageSerializerFactoryTest {
	@Test
	public void testInitJAXB() {
		MessageSerializerFactory.getJAXBContext();
	}
}
