package loc.filter;

public interface FilterSerializer {
	String serialize(Filter filter) throws Exception;
	Filter serialize(String string) throws Exception;
}
