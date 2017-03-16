package loc.filter;

import loc.filter.Filter;

public interface IFilterSerializer {
	String serialize() throws Exception;
	IFilter parse(String string) throws Exception;
}
