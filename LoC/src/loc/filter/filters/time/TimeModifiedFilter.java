package loc.filter.filters.time;

import loc.filter.Filter;

public abstract class TimeModifiedFilter implements Filter {
	public final long timeBound;

	protected TimeModifiedFilter(long timeBound) {
		this.timeBound = timeBound;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TimeModifiedFilter that = (TimeModifiedFilter) o;

		return timeBound == that.timeBound;
	}
}
