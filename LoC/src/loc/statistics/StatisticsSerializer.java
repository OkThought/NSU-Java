package loc.statistics;

import loc.SerializeException;
import loc.filter.*;

import java.util.*;

public class StatisticsSerializer implements IStatisticsSerializer {
	public static char space              = ' ';
	public static String separator        = " - ";
	public static String lineBreak        = "\n";
	public static char underlineCharacter = '-';

	private Statistics statistics;
	private SortedSet<Map.Entry<Filter, FileStatistics>> sortedStatistics;
	private StringBuffer buffer;
	private List<String> filterStrings;
	private List<FileStatistics> fileStatistics;
	private int maxLineCountLength = 0;
	private int maxFileCountLength = 0;

	public StatisticsSerializer(Statistics statistics) {
		this.statistics = statistics;
		sortedStatistics = new TreeSet<>(new StatisticsComparator());
		sortedStatistics.addAll(statistics.getFilterFileStatisticsMap().entrySet());
	}

	@Override
	public String serialize() throws SerializeException {
		buffer = new StringBuffer();
		filterStrings = new ArrayList<>();
		fileStatistics = new ArrayList<>();
		int maxFilterStringLength = 0;
		for (Map.Entry<Filter, FileStatistics> entry: sortedStatistics) {
			Filter filter = entry.getKey();
			String filterString = FilterFactory.getSerializer(filter).serialize(filter);
			filterStrings.add(filterString);
			fileStatistics.add(entry.getValue());
			maxFilterStringLength = Math.max(maxFilterStringLength, filterString.length());
		}
		appendSerialized(maxFilterStringLength);
		return buffer.toString();
	}

	private void calculateMaxLineAndFileCounts() {
		int maxLineCount = 0;
		int maxFileCount = 0;
		for (FileStatistics stats: fileStatistics) {
			maxLineCount = Math.max(maxLineCount, stats.lineCount);
			maxFileCount = Math.max(maxFileCount, stats.fileCount);
		}
		maxLineCountLength = String.valueOf(maxLineCount).length();
		maxFileCountLength = String.valueOf(maxFileCount).length();
	}

	private void appendSerialized(int maxFilterStringLength) {
		buffer.append("Total");
		buffer.append(separator);
		buffer.append(statistics.getTotalLineCount());
		buffer.append(" lines in ");
		buffer.append(statistics.getTotalFileCount());
		buffer.append(" files");
		buffer.append(lineBreak);
		append(underlineCharacter, maxFilterStringLength);
		buffer.append(lineBreak);

		calculateMaxLineAndFileCounts();

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
		append(space, maxLineCountLength - String.valueOf(fileStatistics.lineCount).length());
		buffer.append(" lines in ");
		buffer.append(fileStatistics.fileCount);
		append(space, maxFileCountLength - String.valueOf(fileStatistics.fileCount).length());
		buffer.append(" files");
	}
}
