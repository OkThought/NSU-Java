package loc.statistics;

public class FileStatistics {
    public int fileCount;
    public int lineCount;

    @Override
    public String toString() {
        return lineCount + " lines in " + fileCount + " files";
    }
}
