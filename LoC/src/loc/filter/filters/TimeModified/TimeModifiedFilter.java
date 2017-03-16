package loc.filter.filters.TimeModified;

public abstract class ModifiedTimeFilter implements IFilter {
	public final long timeBound;

	protected ModifiedTimeFilter(long timeBound) {
		this.timeBound = timeBound;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ModifiedTimeFilter that = (ModifiedTimeFilter) o;

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
