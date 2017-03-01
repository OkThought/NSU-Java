package bogush;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

public class StatisticsCollector extends SimpleFileVisitor<Path> {
    private Statistics stats;
    private Filter[] filters;
    public boolean printVisited = false;
    StatisticsCollector(Statistics statistics, Filter[] filters) {
        stats = statistics;
        this.filters = filters;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (attrs.isRegularFile()) {
            if (printVisited)
                System.out.println("\t" + file.getFileName() + ": " + attrs.lastModifiedTime().to(TimeUnit.SECONDS));
            int lineCount = 0;
            boolean filterPassed = false;
            for (Filter filter: filters) {
                if (filter.check(file)) {
                    if (!filterPassed) {
                        lineCount = LineCounter.count(file);
                        filterPassed = true;
                    }
                    stats.increaseFileCounter(filter, 1);
                    stats.increaseLineCounter(filter, lineCount);
                }
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (printVisited)
            System.out.println(dir + ":");
        return FileVisitResult.CONTINUE;
    }
}
