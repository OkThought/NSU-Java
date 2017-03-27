package loc.filter.filters.time;

import loc.filter.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class ModifiedEarlier extends TimeModifiedFilter {
    private static final char prefix = '<';

	public ModifiedEarlier(long upperBound) {
		super(upperBound);
	}

	@Override
	public boolean check(Path file) throws IOException {
		long lastModified = Files.getLastModifiedTime(file).to(TimeUnit.SECONDS);
		return lastModified < timeBound;
	}

	@Override
	public int hashCode() {
		return (int) (prefix * 37 + (timeBound ^ (timeBound >>> 32)));
	}

	@Override
	public String toString() {
		return prefix + String.valueOf(timeBound);
	}
}
