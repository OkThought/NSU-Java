package loc.filter.filters.Aggregate;

import loc.filter.FilterFactory;
import loc.filter.FilterSerializer;
import loc.filter.Filter;

import java.io.IOException;
import java.nio.file.Path;

public class Not implements Filter {
    public static final char prefix = '!';
	public final Filter filter;


	public static class Serializer implements FilterSerializer {
		@Override
		public Not parse(String string) throws Exception {
			String filterString = new loc.Parser(string).skipSpaces().skipChar(prefix).readToTheEnd();
			return new Not(FilterFactory.create(filterString));
		}

		@Override
		public String serialize(Filter filter) throws Exception {
			Filter subFilter = Not.class.cast(filter).filter;
			return prefix + subFilter.getSerializer().serialize(subFilter);
		}
	}

	@Override
	public FilterSerializer getSerializer() {
		return new Serializer();
	}

	public Not(Filter filter) {
		this.filter = filter;
	}

	@Override
	public boolean check(Path file) throws IOException {
		return !filter.check(file);
	}

    @Override
    public int hashCode() {
        return filter != null ? ~filter.hashCode() : 0;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Not not = (Not) o;

		return filter != null ? filter.equals(not.filter) : not.filter == null;
	}

	@Override
	public String toString() {
		return prefix + filter.toString();
	}

	@Override
	public char getPrefix() {
		return prefix;
	}
}