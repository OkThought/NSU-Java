package loc.filter;

public interface FilterSerializer {
	char open_parenthesis = '(';
	char close_parenthesis = ')';
	char delimiter_symbol = ' ';
	String serialize(Filter filter) throws FilterSerializeException;
	Filter serialize(String string) throws FilterSerializeException;
}
