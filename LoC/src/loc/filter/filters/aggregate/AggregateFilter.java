package loc.filter.filters.aggregate;

import loc.filter.Filter;

import java.util.Arrays;

public abstract class AggregateFilter implements Filter {
	public final Filter[] filters;

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
}
