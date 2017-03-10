package loc.filters;

import java.io.IOException;
import java.nio.file.Path;

public class FilterNot implements Filter {
    public static final char prefix = '!';
	public final Filter filter;

	public FilterNot(Filter filter) {
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
