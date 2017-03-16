package loc.statistics;

import loc.FilterSequenceStringBuffer;
import loc.filter.*;

import java.util.*;

public class StatisticsSerializer implements IStatisticsSerializer {
	public static char space              = ' ';
	public static String separator        = " - ";
	public static String lineBreak        = "\n";
	public static char underlineCharacter = '-';
//	public static int underlineLength     = 10;
	private Statistics statistics;
	private SortedSet<Map.Entry<Filter, FileStatistics>> sortedStatistics;
	private StringBuffer buffer;
	private List<String> filterStrings;
	private List<FileStatistics> fileStatistics;

	public StatisticsSerializer(Statistics statistics) {
		this.statistics = statistics;
		sortedStatistics = new TreeSet<>(new StatisticsComparator());
		sortedStatistics.addAll(statistics.getFilterFileStatisticsMap().entrySet());
	}

	@Override
	public String serialize() throws Exception {
		buffer = new StringBuffer();
		filterStrings = new ArrayList<>();
		fileStatistics = new ArrayList<>();
		int maxFilterStringLength = 0;
		for (Map.Entry<Filter, FileStatistics> entry: sortedStatistics) {
			Filter filter = entry.getKey();
			String filterString = filter.getSerializer().serialize(filter);
			filterStrings.add(filterString);
			fileStatistics.add(entry.getValue());
			maxFilterStringLength = Math.max(maxFilterStringLength, filterString.length());
		}
		appendSerialized(maxFilterStringLength);
		return buffer.toString();
	}

	private void appendSerialized(int maxFilterStringLength) {
		buffer.append("Total - ");
		buffer.append(statistics.getTotalLines());
		buffer.append(" lines in ");
		buffer.append(statistics.totalFiles());
		buffer.append(" files");
		buffer.append('\n');
		append(underlineCharacter, maxFilterStringLength);
		buffer.append('\n');
		for (int i = 0; i < filterStrings.size(); i++) {
			String filterString = filterStrings.get(i);
			buffer.append(filterString);
			append(space, maxFilterStringLength - filterString.length());
			buffer.append(separator);
			append(fileStatistics.get(i));
			buffer.append(lineBreak);
		}
	}

	private void append(char c, int amount) {
		for (int i = 0; i < amount; i++) {
			buffer.append(c);
		}
	}

	private void append(FileStatistics fileStatistics) {
		buffer.append(fileStatistics.lineCount);
		buffer.append(" \tlines in ");
		buffer.append(fileStatistics.fileCount);
		buffer.append(" \tfiles");
	}
}
