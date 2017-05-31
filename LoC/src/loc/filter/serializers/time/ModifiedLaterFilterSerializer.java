package loc.filter.serializers.time;

import loc.filter.Filter;
import loc.filter.FilterSerializeException;
import loc.filter.FilterSerializer;
import loc.filter.FilterStringStream;
import loc.filter.filters.time.ModifiedLater;

public class ModifiedLaterFilterSerializer implements FilterSerializer {
	public static final char prefix = '>';
	private static ModifiedLaterFilterSerializer serializer = null;

	private ModifiedLaterFilterSerializer() {
		serializer = this;
	}

	@Override
	public ModifiedLater serialize(String string) throws FilterSerializeException {
		String timestampString = new FilterStringStream(string)
				.skipWhitespaces()
				.skip(prefix)
				.skipWhitespaces()
				.readAll();
		return new ModifiedLater(Long.parseLong(timestampString));
	}

	@Override
	public String serialize(Filter filter) {
		return String.valueOf(prefix) + ModifiedLater.class.cast(filter).timeBound;
	}

	public static synchronized ModifiedLaterFilterSerializer getInstance() {
		if (serializer == null) {
			serializer = new ModifiedLaterFilterSerializer();
		}
		return serializer;
	}
}