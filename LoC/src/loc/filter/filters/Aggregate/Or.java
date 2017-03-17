package loc.filter.filters.Aggregate;

import loc.filter.*;

import java.io.IOException;
import java.nio.file.Path;

public class Or extends AggregateFilter {
    public static final char prefix = '|';
    public Or(Filter[] filters) {
        super(filters);
    }

	public static class Serializer implements FilterSerializer {
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
