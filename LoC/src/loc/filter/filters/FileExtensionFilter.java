package loc.filter.filters;

import loc.filter.*;

import java.nio.file.Path;

public final class FileExtensionFilter implements Filter {
    public static final char prefix = '.';
    public final String extension;

    public FileExtensionFilter(String ext) {
        extension = ext;
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
}
