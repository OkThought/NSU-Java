import java.nio.file.Path;

public class FilterAnd extends AggregateFilter {
    FilterAnd(Filter[] filters) {
        this.filters = filters;
    }

    @Override
    public boolean check(Path file) {
        for (Filter filter: filters) {
            if (!filter.check(file)) {
                return false;
            }
        }
        return true;
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
                name += " & " + f;
            }
        }
        name += ")";
        return name;
    }
}
