import java.nio.file.Path;

public class FileExtensionFilter implements Filter {
    private String extension;

    FileExtensionFilter(String ext) {
        extension = ext;
    }

    @Override
    public boolean check(Path file) {
        return file.toString().endsWith(extension);
    }

    @Override
    public String toString() {
        return extension;
    }
}
