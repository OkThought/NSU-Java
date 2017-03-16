package loc.filter.filters.TimeModified;

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

	@Override
	public int hashCode() {
		return (int) (getPrefix() * 37 + (timeBound ^ (timeBound >>> 32)));
	}

	@Override
	public String toString() {
		return getPrefix() + String.valueOf(timeBound);
	}
}
