package loc.filter.filters.TimeModified;

import loc.filter.Filter;
import loc.filter.FilterSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class ModifiedEarlier extends TimeModifiedFilter {
    public static final char prefix = '<';

	public static class Serializer implements FilterSerializer {
		@Override
		public ModifiedEarlier serialize(String string) throws Exception {
			String timestampString = new loc.Parser(string)
					.skipSpaces()
					.skipChar(prefix)
					.skipSpaces()
					.readToTheEnd();
			return new ModifiedEarlier(Long.parseLong(timestampString));
		}

		@Override
		public String serialize(Filter filter) throws Exception {
			return String.valueOf(prefix) + ModifiedLater.class.cast(filter).timeBound;
		}
	}

	public ModifiedEarlier(long upperBound) {
		super(upperBound);
	}

	@Override
	public FilterSerializer getSerializer() {
		return new Serializer();
	}

	@Override
	public boolean check(Path file) throws IOException {
		long lastModified = Files.getLastModifiedTime(file).to(TimeUnit.SECONDS);
		return lastModified < timeBound;
	}

	@Override
	public char getPrefix() {
		return prefix;
	}
}
