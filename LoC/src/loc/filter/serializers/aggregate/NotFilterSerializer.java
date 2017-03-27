package loc.filter.serializers.aggregate;

import loc.filter.*;
import loc.filter.filters.aggregate.Not;

public class NotFilterSerializer implements FilterSerializer {
	public static final char prefix = '!';
	private static NotFilterSerializer serializer = null;

	private NotFilterSerializer() {
		serializer = this;
	}

	@Override
	public Not serialize(String string) throws FilterSerializeException {
		String filterString = new FilterStringStream(string).skipWhitespaces().skip(prefix).readAll();
		return new Not(FilterFactory.create(filterString));
	}

	@Override
	public String serialize(Filter filter) throws FilterSerializeException {
		Filter subFilter = Not.class.cast(filter).filter;
		return prefix + FilterFactory.getSerializer(subFilter).serialize(subFilter);
	}

	public static synchronized NotFilterSerializer getInstance() {
		if (serializer == null) {
			serializer = new NotFilterSerializer();
		}
		return serializer;
	}
}