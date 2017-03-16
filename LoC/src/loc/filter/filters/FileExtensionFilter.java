package loc.filter.filters;

import loc.IFilterSerializer;

import java.nio.file.Path;

public final class FileExtensionFilter implements IFilter {
    public static final char prefix = '.';
    public final String extension;

    public static class Serializer implements IFilterSerializer {
        @Override
        public FileExtensionFilter parse(String string) throws Exception {
            String extension = new loc.Parser(string)
                    .skipSpaces()
                    .skipChar(prefix)
                    .skipSpaces()
                    .readToTheEnd();
            return new FileExtensionFilter(extension);
        }

        @Override
        public String serialize() throws Exception {
            return null;
        }
    }

    public FileExtensionFilter(String ext) {
        extension = ext;
    }

    @Override
    public IFilterSerializer getSerializer() {
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
