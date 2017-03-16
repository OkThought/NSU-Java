package loc.filter.filters.Aggregate;

import loc.FilterSequenceStringBuffer;
import loc.filter.Filter;
import loc.filter.FilterSerializer;

import java.util.Arrays;

public abstract class AggregateFilter implements Filter {
	public Filter[] filters;


	public static class Serializer implements FilterSerializer {
		@Override
		public AggregateFilter parse(String string) throws Exception {
			return null;
		}

		@Override
		public String serialize(Filter filter) throws Exception {
			Filter[] filters = AggregateFilter.class.cast(filter).filters;
			return new FilterSequenceStringBuffer().append(filters).toString();
		}
	}

	public AggregateFilter(Filter[] filters) {
		this.filters = filters;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AggregateFilter aggregateFilter = (AggregateFilter) o;

		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(filters, aggregateFilter.filters);
	}

	@Override
	public int hashCode() {
		return getPrefix() * 37 + Arrays.hashCode(filters);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(getPrefix());
		buffer.append('(');
		boolean first = true;
		for (Filter filter: filters) {
			if (!first){
				buffer.append(' ');
			}
			first = false;
			buffer.append(filter.toString());
		}
		buffer.append(')');
		return buffer.toString();
	}
}
