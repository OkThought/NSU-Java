package loc.filter.serializers.aggregate;

import loc.filter.Filter;
import loc.filter.FilterFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotFilterSerializerTest {
	@Test
	public void serializeCustomNot() throws Exception {
		Filter filter = FilterFactory.create("    !    .cpp   ");
		assertNotNull(filter);
		String string = NotFilterSerializer.getInstance().serialize(filter);
		assertNotNull(string);
		assertFalse(string.isEmpty());
	}
}