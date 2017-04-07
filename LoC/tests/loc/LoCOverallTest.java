package loc;

import loc.filter.Filter;
import loc.statistics.FileStatistics;
import loc.statistics.Statistics;
import loc.statistics.StatisticsCollector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

import static org.junit.Assert.*;

public class LoCOverallTest {
	private final String inputDir = "tests/input"; // CAUTION: this dir will be removed after test
	private final String customConfigFile = inputDir + "/config.txt";
	private long minuteBefore;
	private long minuteAfter;

	@Test (expected = StatisticsCollector.NoFilterException.class)
	public void noFilter() throws Exception {
		new StatisticsCollector(new Statistics(), new Filter[0]);
	}

	@Test
	public void custom() throws Exception {
		Statistics statistics = new Statistics();

		Filter[] filters = ConfigFileReader.readFilters(Paths.get(customConfigFile));
		StatisticsCollector collector = new StatisticsCollector(statistics, filters);
		Files.walkFileTree(Paths.get(inputDir), collector);
		Map<Filter, FileStatistics> filterStats = statistics.getFilterFileStatisticsMap();
		assertEquals(6, statistics.getTotalFileCount());
		assertEquals(123, statistics.getTotalLineCount());
		assertEquals(statistics.getTotalFileCount(), filterStats.get(filters[4]).fileCount);
		assertEquals(statistics.getTotalLineCount(), filterStats.get(filters[4]).lineCount);
		assertEquals(statistics.getTotalFileCount(), filterStats.get(filters[7]).fileCount);
		assertEquals(statistics.getTotalLineCount(), filterStats.get(filters[7]).lineCount);
		assertEquals(statistics.getTotalFileCount(), filterStats.get(filters[12]).fileCount);
		assertEquals(statistics.getTotalLineCount(), filterStats.get(filters[12]).lineCount);
		assertEquals(7, filterStats.get(filters[0]).lineCount);
		assertEquals(2, filterStats.get(filters[0]).fileCount);
		assertEquals(2, filterStats.get(filters[1]).lineCount);
		assertEquals(1, filterStats.get(filters[1]).fileCount);
		assertEquals(113, filterStats.get(filters[3]).lineCount);
		assertEquals(2, filterStats.get(filters[3]).fileCount);
		assertEquals(filterStats.get(filters[0]).lineCount, filterStats.get(filters[8]).lineCount);
		assertEquals(filterStats.get(filters[0]).fileCount, filterStats.get(filters[8]).fileCount);
		assertEquals(filterStats.get(filters[1]).lineCount, filterStats.get(filters[9]).lineCount);
		assertEquals(filterStats.get(filters[1]).fileCount, filterStats.get(filters[9]).fileCount);

		assertFalse(filterStats.containsKey(filters[2]));
		assertFalse(filterStats.containsKey(filters[5]));
		assertFalse(filterStats.containsKey(filters[6]));
		assertFalse(filterStats.containsKey(filters[10]));
		assertFalse(filterStats.containsKey(filters[11]));
	}

	private void prepareCustomConfigFile() throws IOException {
		Files.createDirectories(Paths.get(inputDir));
		long currentSeconds = System.currentTimeMillis() / 1000;
		minuteBefore = currentSeconds - 60;
		minuteAfter = currentSeconds + 60;
		PrintWriter writer = new PrintWriter(customConfigFile, "UTF-8");
		writer.println(".java");                                                        // 0
		writer.println("  .cpp");                                                       // 1
		writer.println("  .c   ");                                                      // 2 always false
		writer.println(".txt   ");                                                      // 3
		writer.println(">" + minuteBefore);                                             // 4 always true
		writer.println(" >" + minuteAfter);                                             // 5 always false
		writer.println("< " + minuteBefore);                                            // 6 always false
		writer.println("   <   " + minuteAfter);                                        // 7 always true
		writer.println("&(   <   " + minuteAfter + "    .java)");                       // 8 ≈ 0
		writer.println("|(   <   " + minuteBefore + "   .cpp   )  ");                   // 9 ≈ 1
		writer.println("  &   (   .java    .cpp   )   ");                               // 10 always false
		writer.println("  &   (  > " + minuteAfter + "  <  " + minuteBefore + "  )   ");// 11 always false
		writer.print  ("  |   (  < " + minuteAfter + "  >  " + minuteBefore + "  )   ");// 12 always true
		writer.close();
	}

	private void prepareCustom() throws IOException {
		prepareCustomConfigFile();
		createCustomFile("0/1/2", "testFile.java", 4);
		createCustomFile("0/1", "testFile.java", 3);
		createCustomFile("0/1", "testFile.cpp", 2);
		createCustomFile("0", "testFile.txt", 100);
		createCustomFile(".", "testFile.other", 1);
	}

	private void createCustomFile(String relativeDir, String filename, int lineNumber) throws IOException {
		String dirPath = inputDir + '/' + relativeDir;
		String filePath = dirPath + "/" + filename;
		Files.createDirectories(Paths.get(dirPath));
		PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		for (int i = 1; i < lineNumber; i++) {
			writer.println(i);
		}
		writer.print(lineNumber);
		writer.close();
	}

	private void removeCustomFiles() throws IOException {
		Files.walkFileTree(Paths.get(inputDir), new FileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return null;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	@Before
	public void setUp() throws Exception {
		prepareCustom();
	}

	@After
	public void tearDown() throws Exception {
		removeCustomFiles();
	}
}