package loc.filter.filters.Aggregate;

import loc.filter.FilterSerializer;
import loc.filter.Filter;

import java.io.IOException;
import java.nio.file.Path;

public class Or extends AggregateFilter {
    public static final char prefix = '|';
    public Or(Filter[] filters) {
        super(filters);
    }

	public static class Serializer implements FilterSerializer {
		@Override
		public Or serialize(String string) throws Exception {
			String filterSequenceString = new loc.Parser(string)
					.skipSpaces()
					.skipChar(prefix)
					.skipSpaces()
					.openParenthesis()
					.getCurrentBufferString();
			Filter[] filters = new loc.Parser(filterSequenceString).parseSequence();
			return new Or(filters);
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

	@Override
	public boolean check(Path file) throws IOException {
		for (Filter filter: filters) {
			if (filter.check(file)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public char getPrefix() {
		return prefix;
	}
}
