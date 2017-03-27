package loc.filter.serializers.time;

import loc.filter.Filter;
import loc.filter.FilterSerializeException;
import loc.filter.FilterSerializer;
import loc.filter.FilterStringStream;
import loc.filter.filters.time.ModifiedEarlier;
import loc.filter.filters.time.ModifiedLater;

public class ModifiedEarlierFilterSerializer implements FilterSerializer {
	public static final char prefix = '<';
	private static ModifiedEarlierFilterSerializer serializer = null;

	private ModifiedEarlierFilterSerializer() {
		serializer = this;
	}

	@Override
	public ModifiedEarlier serialize(String string) throws FilterSerializeException {
		String timestampString = new FilterStringStream(string)
				.skipWhitespaces()
				.skip(prefix)
				.skipWhitespaces()
				.readAll();
		return new ModifiedEarlier(Long.parseLong(timestampString));
	}

	@Override
	public String serialize(Filter filter) {
		return String.valueOf(prefix) + ModifiedLater.class.cast(filter).timeBound;
	}

	public static synchronized ModifiedEarlierFilterSerializer getInstance() {
		if (serializer == null) {
			serializer = new ModifiedEarlierFilterSerializer();
		}
		return serializer;
	}
}
