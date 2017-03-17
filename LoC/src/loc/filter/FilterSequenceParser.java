package loc.filter;

import java.util.ArrayList;
import java.util.List;

public class FilterSequenceParser {
	public static final char __openParenthesis  = '(';
	public static final char __closeParenthesis = ')';
	public static final char __separator        = ' ';

	private FilterStringStream stream;
	private List<Filter> filterList;

	public FilterSequenceParser(String sequenceString) {
		stream = new FilterStringStream(sequenceString);
		filterList = new ArrayList<>();
	}

	public Filter[] parse() throws FilterSerializeException {
		// [ ]prefix[ ]filter1[ ][ [ ]prefix[ ]filter2[ ][ ...]]
		int prefix;
		while (!stream.endReached()) {
			if ((prefix = stream.skipWhitespaces().readChar()) == -1) break;
			String filterString = (char)prefix + stream.clearBuffer().readWord();
			Filter filter = FilterFactory.create(filterString);
			filterList.add(filter);
		}
		if (filterList.isEmpty()) throw new FilterSerializeException("Empty filter sequence");
		return filterList.toArray(new Filter[]{});
	}
}
