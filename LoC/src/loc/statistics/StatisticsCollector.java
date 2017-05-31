package loc.statistics;

import loc.filter.Filter;
import loc.LineCounter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.*;

public class StatisticsCollector extends SimpleFileVisitor<Path> {
	public static class NoFilterException extends Exception {
		public NoFilterException(String message) {
			super(message);
		}
	}

	private Statistics stats;
	private Filter[] filters;
	public StatisticsCollector(Statistics statistics, Filter[] filters) throws NoFilterException {
		if (statistics == null) throw new NullPointerException("Statistics is null");
		if (filters == null) throw new NullPointerException("Filter array is null");
		if (filters.length == 0) throw new NoFilterException("No filter passed");
		stats = statistics;
		this.filters = filters;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return SKIP_SUBTREE;
	}

	@Override
	public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
		try {
			if (attrs.isRegularFile()) {
				int lineCount = 0;
				boolean filterPassed = false;
				for (Filter filter : filters) {
					if (filter.check(filePath)) {
						if (!filterPassed) {
							lineCount = new LineCounter(filePath).count();
							filterPassed = true;
						}
						stats.increaseLineCounter(filePath, filter, lineCount);
					}
				}
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return CONTINUE;
	}
}
