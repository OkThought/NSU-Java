package loc.filter.filters;

import loc.filter.*;

import java.nio.file.Path;

public final class FileExtensionFilter implements Filter {
    public static final char prefix = '.';
    public final String extension;

    public static class Serializer implements FilterSerializer {
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
    }

    public FileExtensionFilter(String ext) {
        extension = ext;
    }

    @Override
    public FilterSerializer getSerializer() {
        return new Serializer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileExtensionFilter that = (FileExtensionFilter) o;

        return extension != null ? extension.equals(that.extension) : that.extension == null;
    }

    @Override
    public int hashCode() {
        return extension != null ? extension.hashCode() : 0;
    }

    @Override
    public boolean check(Path file) {
        return file.toString().endsWith(prefix + extension);
    }

    @Override
    public String toString() {
        return prefix + extension;
    }

	@Override
	public char getPrefix() {
		return prefix;
	}
}
