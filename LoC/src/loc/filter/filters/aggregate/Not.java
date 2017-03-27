package loc.filter.filters.aggregate;

import loc.filter.*;

import java.io.IOException;
import java.nio.file.Path;

public class Not implements Filter {
    private static final char prefix = '!';
	public final Filter filter;

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
}
