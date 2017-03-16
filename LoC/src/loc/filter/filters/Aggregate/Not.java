package loc.filter.filters.Aggregate;

import loc.FilterFactory;
import loc.filter.FilterSerializer;

import java.io.IOException;
import java.nio.file.Path;

public class Not implements Filter {
    public static final char prefix = '!';
	public final Filter filter;


	public static class Serializer implements FilterSerializer {
		@Override
		public FilterNot parse(String string) throws Exception {
			String filterString = new loc.Parser(string).skipSpaces().skipChar(prefix).readToTheEnd();
			return new FilterNot(FilterFactory.create(filterString));
		}

		@Override
		public String serialize() throws Exception {
			return null;
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

		FilterNot filterNot = (FilterNot) o;

		return filter != null ? filter.equals(filterNot.filter) : filterNot.filter == null;
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
