
import loc.IFilterSerializer;
package loc.filter;

import java.io.IOException;
import java.nio.file.Path;

public interface IFilter {
    boolean check(Path file) throws IOException;
	char getPrefix();
	IFilterSerializer getSerializer();
}
