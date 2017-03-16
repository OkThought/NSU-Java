package loc.filter.filters.TimeModified;

import loc.filter.Filter;
import loc.filter.FilterSerializer;
import loc.filter.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class ModifiedLater extends TimeModifiedFilter {
    public static final char prefix = '>';

	public static class Serializer implements FilterSerializer {
		@Override
		public ModifiedLater serialize(String string) throws Parser.ParseException {
			String timestampString = new Parser(string)
					.skipSpaces()
					.skipChar(prefix)
					.skipSpaces()
					.readToTheEnd();
			return new ModifiedLater(Long.parseLong(timestampString));
		}

		@Override
		public String serialize(Filter filter) {
			return String.valueOf(prefix) + ModifiedLater.class.cast(filter).timeBound;
		}
	}

	public ModifiedLater(long lowerBound) {
		super(lowerBound);
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
