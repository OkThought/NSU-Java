package loc.statistics;

import loc.filter.Filter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Statistics {
    private int totalLines;
    private Set<Path> pathSet = new HashSet<>();
    private Map<Filter, FileStatistics> filterFileStatisticsMap = new HashMap<>();

    void increaseLineCounter(Path filePath, Filter filter, int amount) {
        if (!pathSet.contains(filePath)) {
	        pathSet.add(filePath);
	        totalLines += amount;
        }
	    FileStatistics fileStatistics;
	    if (!filterFileStatisticsMap.containsKey(filter)) {
	        fileStatistics = new FileStatistics();
	        filterFileStatisticsMap.put(filter, fileStatistics);
        } else {
	        fileStatistics = filterFileStatisticsMap.get(filter);
        }
	    fileStatistics.fileCount += 1;
	    fileStatistics.lineCount += amount;
    }

    public int getTotalLineCount() {
        return totalLines;
    }

    public int getTotalFileCount() {
        return pathSet.size();
    }

    public Map<Filter, FileStatistics> getFilterFileStatisticsMap() {
        return filterFileStatisticsMap;
    }

    @Override
    public String toString() {
        String string = "Total " + totalLines + " lines in " + getTotalFileCount() + " files";
        return string;
    }
}
