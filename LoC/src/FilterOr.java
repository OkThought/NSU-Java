import java.nio.file.Path;

public class FilterOr extends AggregateFilter {
    FilterOr(Filter[] filters) {
        this.filters = filters;
    }
    
    @Override
    public boolean check(Path file) {
        for (Filter filter: filters) {
            if (filter.check(file)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String name = "";
        boolean first = true;
        for (Filter f: filters) {
            if (first) {
                first = false;
                name = "(" + f;
            } else {
                name += " || " + f;
            }
        }
        name += ")";
        return name;
    }
}
