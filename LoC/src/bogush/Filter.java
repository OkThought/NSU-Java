package bogush;

import java.nio.file.Path;

public interface Filter {
    boolean check(Path file);
}
