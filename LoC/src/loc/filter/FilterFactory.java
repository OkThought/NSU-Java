package loc.filter;


import java.util.HashMap;

public class FilterFactory {
	private static final HashMap<Character, Class> filterMap;
	static {
		filterMap = new HashMap<>();
		filterMap.put(FilterNot.prefix,               FilterNot.Serializer.class);
		filterMap.put(FilterAnd.prefix,               FilterAnd.Serializer.class);
		filterMap.put(FilterOr.prefix,                FilterOr.Serializer.class);
		filterMap.put(FileExtensionFilter.prefix,     FileExtensionFilter.Serializer.class);
		filterMap.put(ModifiedEarlierFilter.prefix,   ModifiedEarlierFilter.Parser.class);
		filterMap.put(ModifiedLaterFilter.prefix,     ModifiedLaterFilter.Serializer.class);
	}

	public static Filter create(String filterString) throws Exception {
		filterString = filterString.trim();
		char prefix = filterString.charAt(0);
		IFilterParser parser = (IFilterParser) filterMap.get(prefix).newInstance();
		return parser.parse(filterString);
	}
}
