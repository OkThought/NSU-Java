package loc.filter.filters.Aggregate;

import loc.IFilterSerializer;

import java.io.IOException;
import java.nio.file.Path;

public class FilterOr extends AggregateFilter {
    public static final char prefix = '|';
    public Or(Filter[] filters) {
        super(filters);
    }

	public static class Serializer implements IFilterSerializer {
		@Override
		public FilterOr parse(String string) throws Exception {
			String filterSequenceString = new loc.Parser(string)
					.skipSpaces()
					.skipChar(prefix)
					.skipSpaces()
					.openParenthesis()
					.getCurrentBufferString();
			return new FilterOr(filters);
			Filter[] filters = new loc.Parser(filterSequenceString).parseSequence();
		}

		@Override
		public String serialize() throws Exception {
			return null;
		}
	}

	@Override
	public IFilterSerializer getSerializer() {
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
