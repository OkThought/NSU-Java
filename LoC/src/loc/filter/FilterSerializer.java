package loc.filter;

import loc.filter.Filter;

public interface FilterSerializer {
	String serialize(Filter filter) throws Exception;
	Filter parse(String string) throws Exception;
}
