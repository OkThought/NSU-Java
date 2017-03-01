package bogush;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeFilterGreater extends TimeFilter {
    TimeFilterGreater (long lowerBound) {
        super(lowerBound);
    }

    @Override
    public boolean check(Path file) {
        long lastModified = 0;
        try {
            lastModified = Files.getLastModifiedTime(file).to(TimeUnit.SECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lastModified > timeBound;
    }

    @Override
    public String toString() {
        return "modified later than " + new Date(timeBound * 1000).toString();
    }
}
