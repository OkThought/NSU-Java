package loc.filter.filters.aggregate;

import loc.filter.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

public class And extends AggregateFilter {
	private static final char __prefix = '&';
	private static final char __open_parenthesis = '(';
	private static final char __close_parenthesis = ')';
	private static final char __delimiter = ' ';

	public And(Filter[] filters) {
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
	public int hashCode() {
		return __prefix * 37 + Arrays.hashCode(filters);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(__prefix);
		buffer.append(__open_parenthesis);
		boolean first = true;
		for (Filter filter: filters) {
			if (!first){
				buffer.append(__delimiter);
			}
			first = false;
			buffer.append(filter.toString());
		}
		buffer.append(__close_parenthesis);
		return buffer.toString();
	}
}
