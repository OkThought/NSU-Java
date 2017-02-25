import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private int totalLines;
    private int totalFiles;
    private Map<Filter, FileStatistics> filterFileStatisticsMap;

    Statistics() {
        filterFileStatisticsMap = new HashMap<>();
    }

    void increaseLineCounter(Filter filter, int amount) {
        if (amount == 0) {
            return;
        }
        filterFileStatisticsMap.putIfAbsent(filter, new FileStatistics());
        filterFileStatisticsMap.get(filter).lineCount += amount;
        totalLines += amount;
    }

    void increaseFileCounter(Filter filter, int amount) {
        if (amount == 0) {
            return;
        }
        filterFileStatisticsMap.putIfAbsent(filter, new FileStatistics());
        filterFileStatisticsMap.get(filter).fileCount += amount;
        totalFiles += amount;
    }

    @Override
    public String toString() {
        String string = "Total " + totalLines + " lines in " + totalFiles + " files:\n";
        for (Map.Entry<Filter, FileStatistics> pair: filterFileStatisticsMap.entrySet()) {
            string += "\t" + pair.getKey() + ": " + pair.getValue() + "\n";
        }
        return string;
    }
}
