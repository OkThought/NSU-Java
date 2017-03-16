package loc.filter;

import java.io.IOException;
import java.nio.file.Path;

public interface Filter {
    boolean check(Path file) throws IOException;
	char getPrefix();
	IFilterSerializer getSerializer();
}
