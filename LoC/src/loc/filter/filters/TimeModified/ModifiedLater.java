package loc.filter.filters.TimeModified;

import loc.filter.FilterSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class LaterModified extends TimeModifiedFilter {
    public static final char prefix = '>';
    public LaterModified(long lowerBound) {
        super(lowerBound);
    }


	public static class Serializer implements FilterSerializer {
		@Override
		public LaterModified parse(String string) throws Exception {
			String timestampString = new loc.Parser(string)
					.skipSpaces()
					.skipChar(prefix)
					.skipSpaces()
					.readToTheEnd();
			return new LaterModified(Long.parseLong(timestampString));
		}

		@Override
		public String serialize() throws Exception {
			return null;
		}
	}

	@Override
	public FilterSerializer getSerializer() {
		return new Serializer();
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
