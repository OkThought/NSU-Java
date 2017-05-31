package loc.filter.serializers;

import loc.filter.Filter;
import loc.filter.FilterSerializeException;
import loc.filter.FilterSerializer;
import loc.filter.FilterStringStream;
import loc.filter.filters.FileExtensionFilter;

public class FileExtensionFilterSerializer implements FilterSerializer {
	public static final char prefix = '.';
	private static FileExtensionFilterSerializer serializer = null;

	private FileExtensionFilterSerializer() {
		serializer = this;
	}

	@Override
	public FileExtensionFilter serialize(String string) throws FilterSerializeException {
		String extension = new FilterStringStream(string)
				.skipWhitespaces()
				.skip(prefix)
				.skipWhitespaces()
				.readAll();
		return new FileExtensionFilter(extension);
	}

	@Override
	public String serialize(Filter filter) {
		return prefix + FileExtensionFilter.class.cast(filter).extension;
	}

	public static synchronized FileExtensionFilterSerializer getInstance() {
		if (serializer == null) {
			serializer = new FileExtensionFilterSerializer();
		}
		return serializer;
	}
}
