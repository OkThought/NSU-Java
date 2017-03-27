package loc.statistics;

import loc.filter.Filter;
import loc.LineCounter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

public class StatisticsCollector extends SimpleFileVisitor<Path> {
	public static class NoFilterException extends Exception {
		public NoFilterException(String message) {
			super(message);
		}
	}

	private Statistics stats;
	private Filter[] filters;
	public boolean printVisited = false;
	public StatisticsCollector(Statistics statistics, Filter[] filters) throws NoFilterException {
		if (filters.length == 0) throw new NoFilterException("No filter passed");
		stats = statistics;
		this.filters = filters;
	}

	@Override
	public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
//		try {
			if (attrs.isRegularFile()) {
				if (printVisited)
					System.out.println("\t" + filePath.getFileName() + ": " + attrs.lastModifiedTime().to(TimeUnit.SECONDS));
				int lineCount = 0;
				boolean filterPassed = false;
				for (Filter filter : filters) {
					if (filter.check(filePath)) {
						if (!filterPassed) {
							lineCount = LineCounter.count(filePath);
							filterPassed = true;
						}
						stats.increaseLineCounter(filePath, filter, lineCount);
					}
				}
			}
//		} catch (IOException e) {
//			System.err.println(e.getMessage());
//		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		if (printVisited)
			System.out.println(dir + ":");
		return FileVisitResult.CONTINUE;
	}
}
