package loc.filter.filters.Aggregate;

import java.util.Arrays;

public abstract class AggregateFilter implements IFilter {
	public IFilter[] filters;

	public AggregateFilter(IFilter[] filters) {
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
		StringBuffer buffer = new StringBuffer();
		buffer.append(getPrefix());
		buffer.append('(');
		boolean first = true;
		for (IFilter filter: filters) {
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
