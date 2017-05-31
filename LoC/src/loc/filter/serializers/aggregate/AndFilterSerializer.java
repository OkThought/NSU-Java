package loc.filter.serializers.aggregate;

import loc.filter.*;
import loc.filter.filters.aggregate.And;

public class AndFilterSerializer implements FilterSerializer {
	public static final char prefix = '&';
	private static AndFilterSerializer serializer = null;

	private AndFilterSerializer() { serializer = this; }

	@Override
	public And serialize(String string) throws FilterSerializeException {
		String filterSequenceString = new FilterStringStream(string)
				.skipWhitespaces()
				.skip(prefix)
				.skipWhitespaces()
				.openParenthesis()
				.toString();
		Filter[] filters = new FilterSequenceParser(filterSequenceString).parse();
		return new And(filters);
	}

	@Override
	public String serialize(Filter filter) throws FilterSerializeException {
		return new FilterStringStream()
				.append(prefix)
				.append(And.class.cast(filter).filters)
				.toString();
	}

	public static synchronized FilterSerializer getInstance() {
		if (serializer == null) {
			serializer = new AndFilterSerializer();
		}
		return serializer;
	}
}
