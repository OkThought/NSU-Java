package loc;

import loc.filters.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class FilterFactory {
	public static final Class<FilterParser> filterParser = FilterParser.class;
	private static final HashMap<Character, String> parsers;
	static {
		parsers = new HashMap<>();
		parsers.put(FilterNot.prefix,               "parseNot");
		parsers.put(FilterAnd.prefix,               "parseAnd");
		parsers.put(FilterOr.prefix,                "parseOr");
		parsers.put(FileExtensionFilter.prefix,     "parseFileExtension");
		parsers.put(ModifiedEarlierFilter.prefix,          "parseTimeLess");
		parsers.put(ModifiedLaterFilter.prefix,       "parseTimeGreater");
	}

	public static Filter create(String filterString) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		filterString = filterString.trim();
		char prefix = filterString.charAt(0);
		Method parser = filterParser.getMethod(parsers.get(prefix), String.class);
		filterString = filterString.substring(1);
		return (Filter) parser.invoke(null, filterString);
	}
}
