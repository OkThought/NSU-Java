package loc;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LineCounter {
	public static final int DEFAULT_BUFFER_SIZE = 1024;
	private Path filePath;
	private File file;

	public LineCounter(Path filePath) {
		this.filePath = filePath;
		file = filePath.toFile();
	}

	public LineCounter(String filePath) {
		this.filePath = Paths.get(filePath);
		file = this.filePath.toFile();
	}

	public int count(int bufferSize) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filePath.toFile()))) {
            boolean empty = true;
            byte bytes[] = new byte[bufferSize];
            int readChars;
            int count = 1;
            while ((readChars = is.read(bytes)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (bytes[i] == '\n') {
                        ++count;
                    }
                }
            }
            if (empty) {
                return 0;
            } else {
                return count;
            }
        }
	}

	public int count() throws IOException {
		return count(DEFAULT_BUFFER_SIZE);
	}
}
