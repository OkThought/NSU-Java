package loc;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.*;

public class LineCounterTest {
	@Test (expected = IOException.class)
	public void inexistantFile() throws Exception {
		new LineCounter("no file").count();
	}

	@Test
	public void customFile() throws IOException {
		int count = new LineCounter("tests/loc/LineCounterTest.java").count();
		assertEquals(22, count);
	}
}