package loc.statistics;

import loc.filters.Filter;

import java.util.Comparator;
import java.util.Map;

public class StatisticsComparator implements Comparator<Map.Entry<Filter, FileStatistics>> {
@Override
	public int compare(Map.Entry<Filter, FileStatistics> o1, Map.Entry<Filter, FileStatistics> o2) {
		if (o1 == null || o2 == null) throw new NullPointerException();
		if (o1.getValue().lineCount < o2.getValue().lineCount) return 1;
		if (o1.getValue().lineCount > o2.getValue().lineCount) return -1;
		if (o1.getValue().fileCount < o2.getValue().fileCount) return 1;
		return -1;
	}
}
