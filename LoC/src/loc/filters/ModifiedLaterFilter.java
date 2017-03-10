package loc.filters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class ModifiedLaterFilter extends ModifiedTimeFilter {
    public static final char prefix = '>';
    public ModifiedLaterFilter(long lowerBound) {
        super(lowerBound);
    }

	@Override
	public boolean check(Path file) throws IOException {
		long lastModified = Files.getLastModifiedTime(file).to(TimeUnit.SECONDS);
		return lastModified > timeBound;
	}

	@Override
	public char getPrefix() {
		return prefix;
	}
}
