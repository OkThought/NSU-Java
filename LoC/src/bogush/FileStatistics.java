package bogush;

public class FileStatistics {
    int fileCount;
    int lineCount;

    @Override
    public String toString() {
        return lineCount + " lines in " + fileCount + " files";
    }
}
