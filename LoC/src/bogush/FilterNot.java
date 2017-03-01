package bogush;

import java.nio.file.Path;

public class FilterNot implements Filter {
    private Filter filter;

    FilterNot(Filter filter) {
        this.filter = filter;
    }

    @Override
    public boolean check(Path file) {
        return !filter.check(file);
    }

    @Override
    public String toString() {
        return "not " + filter;
    }
}
