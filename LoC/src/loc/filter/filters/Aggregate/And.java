package loc.filter.filters.Aggregate;

import loc.filter.*;

import java.io.IOException;
import java.nio.file.Path;

public class And extends AggregateFilter {
	public static final char prefix = '&';

	public static class Serializer implements FilterSerializer {
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
			return new AggregateFilter.Serializer().serialize(filter);
		}
	}

	@Override
	public FilterSerializer getSerializer() {
		return new Serializer();
	}

	public And(Filter[] filters) {
        super(filters);
    }

	@Override
	public boolean check(Path file) throws IOException {
		for (Filter filter: filters) {
			if (!filter.check(file)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public char getPrefix() {
		return prefix;
	}
}
