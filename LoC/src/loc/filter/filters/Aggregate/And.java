package loc.filter.filters.Aggregate;

import loc.filter.FilterSerializer;
import loc.filter.Filter;

import java.io.IOException;
import java.nio.file.Path;

public class And extends AggregateFilter {
	public static final char prefix = '&';

	public static class Serializer implements FilterSerializer {
		@Override
		public And serialize(String string) throws Exception {
			String filterSequenceString = new loc.Parser(string)
					.skipSpaces()
					.skipChar(prefix)
					.skipSpaces()
					.openParenthesis()
					.getCurrentBufferString();
			Filter[] filters = new loc.Parser(filterSequenceString).parseSequence();
			return new And(filters);
		}

		@Override
		public String serialize(Filter filter) throws Exception {
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
