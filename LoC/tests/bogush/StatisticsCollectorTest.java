package bogush;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class StatisticsCollectorTest {
    @Test (expected = NoFilterException.class)
    public void noFilter() throws Exception {
        new StatisticsCollector(new Statistics(), new Filter[0]);
    }

    @Test
    public void custom() throws Exception {
        Statistics statistics = new Statistics();
        Filter[] filters = FilterParser.parse(Paths.get("input/filters"));
        StatisticsCollector collector = new StatisticsCollector(statistics, filters);
        Files.walkFileTree(Paths.get("input/dir"), collector);
        assertEquals(4, statistics.getTotalFiles());
        assertEquals(10, statistics.getTotalLines());

        assertEquals(1, statistics.getFilterFileStatisticsMap().get(filters[0]).fileCount); // .cpp
        assertEquals(3, statistics.getFilterFileStatisticsMap().get(filters[0]).lineCount);
        assertEquals(1, statistics.getFilterFileStatisticsMap().get(filters[1]).fileCount); // .java
        assertEquals(4, statistics.getFilterFileStatisticsMap().get(filters[1]).lineCount);
        assertEquals(2, statistics.getFilterFileStatisticsMap().get(filters[2]).fileCount); // Or(.cpp, .java)
        assertEquals(7, statistics.getFilterFileStatisticsMap().get(filters[2]).lineCount);
        assertEquals(0, statistics.getFilterFileStatisticsMap().get(filters[3]).fileCount); // And(.cpp, .java)
        assertEquals(0, statistics.getFilterFileStatisticsMap().get(filters[3]).lineCount);
        assertEquals(10, statistics.getFilterFileStatisticsMap().get(filters[4]).fileCount); // >1287311235
        assertEquals(10, statistics.getFilterFileStatisticsMap().get(filters[4]).lineCount);
        assertEquals(0, statistics.getFilterFileStatisticsMap().get(filters[5]).fileCount); // <1287311235
        assertEquals(0, statistics.getFilterFileStatisticsMap().get(filters[5]).lineCount);
    }
}