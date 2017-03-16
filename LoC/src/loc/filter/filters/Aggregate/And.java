package loc.filter.filters.Aggregate;

import loc.IFilterSerializer;

import java.io.IOException;
import java.nio.file.Path;

public class FilterAnd extends AggregateFilter {
	public static final char prefix = '&';

	public static class Serializer implements IFilterSerializer {
		@Override
		public FilterAnd parse(String string) throws Exception {
			String filterSequenceString = new loc.Parser(string)
					.skipSpaces()
					.skipChar(prefix)
					.skipSpaces()
					.openParenthesis()
					.getCurrentBufferString();
			return new FilterAnd(filters);
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

	public FilterAnd(IFilter[] filters) {
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
