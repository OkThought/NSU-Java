package loc.filters;

import java.io.IOException;
import java.nio.file.Path;

public class FilterOr extends AggregateFilter {
    public static final char prefix = '|';
    public FilterOr(Filter[] filters) {
        super(filters);
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
