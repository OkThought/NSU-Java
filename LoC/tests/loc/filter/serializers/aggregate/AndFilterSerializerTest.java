package loc.filter.serializers.aggregate;

import loc.filter.Filter;
import loc.filter.FilterFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class AndFilterSerializerTest {
	@Test
	public void serializeCustomAnd() throws Exception {
		Filter filter = FilterFactory.create("  &  ( & (.c  .h)  &(.cpp  .hpp))");
		assertNotNull(filter);
		String string = AndFilterSerializer.getInstance().serialize(filter);
		assertNotNull(string);
		assertFalse(string.isEmpty());
	}
}