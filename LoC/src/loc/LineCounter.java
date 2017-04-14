package loc;

import java.io.*;
import java.nio.file.Path;

public class LineCounter {
	public static final int defaultBufferSize = 1024;

	public static int count(Path filePath, int bufferSize) throws IOException {
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

	public static int count(Path filePath) throws IOException {
		return count(filePath, defaultBufferSize);
	}
}
