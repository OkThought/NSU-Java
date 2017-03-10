package loc;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;

public class LineCounterTest {
    @Test (expected = IOException.class)
    public void inexistantFile() throws Exception {
        LineCounter.count(Paths.get("no file"));
    }

    @Test
    public void customFile() throws IOException {
        Path path = Paths.get("tests/input/10lines");
        int count = LineCounter.count(path);
        assertEquals(10, count);
    }
}