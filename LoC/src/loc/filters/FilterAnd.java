package loc.filters;

import java.io.IOException;
import java.nio.file.Path;

public class FilterAnd extends AggregateFilter {
	public static final char prefix = '&';
    public FilterAnd(Filter[] filters) {
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
