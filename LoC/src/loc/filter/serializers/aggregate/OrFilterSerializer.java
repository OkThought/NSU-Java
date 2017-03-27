package loc.filter.serializers.aggregate;

import loc.filter.*;
import loc.filter.filters.aggregate.AggregateFilter;
import loc.filter.filters.aggregate.And;
import loc.filter.filters.aggregate.Or;

public class OrFilterSerializer implements FilterSerializer {
	public static final char prefix = '|';
	private static OrFilterSerializer serializer = null;

	private OrFilterSerializer() {
		serializer = this;
	}

	@Override
	public Or serialize(String string) throws FilterSerializeException {
		String filterSequenceString = new FilterStringStream(string)
				.skipWhitespaces()
				.skip(prefix)
				.skipWhitespaces()
				.openParenthesis()
				.toString();
		Filter[] filters = new FilterSequenceParser(filterSequenceString).parse();
		return new Or(filters);
	}

	@Override
	public String serialize(Filter filter) throws FilterSerializeException {
		return new FilterStringStream()
				.append(prefix)
				.append(And.class.cast(filter).filters)
				.toString();
	}

	public static synchronized OrFilterSerializer getInstance() {
		if (serializer == null) {
			serializer = new OrFilterSerializer();
		}
		return serializer;
	}
}