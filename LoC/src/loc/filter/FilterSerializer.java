package loc.filter;

public interface FilterSerializer {
	String serialize(Filter filter) throws FilterSerializeException;
	Filter serialize(String string) throws FilterSerializeException;
}
