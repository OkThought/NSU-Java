import org.junit.Test;
import ru.nsu.ccfit.bogush.serialization.MessageSerializerFactory;

public class MessageSerializerFactoryTest {
	@Test
	public void testInitJAXB() {
		MessageSerializerFactory.getJAXBContext();
	}
}
