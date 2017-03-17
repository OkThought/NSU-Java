package loc.filter;

import loc.filter.filters.Aggregate.And;
import loc.filter.filters.Aggregate.Not;
import loc.filter.filters.Aggregate.Or;
import loc.filter.filters.FileExtensionFilter;
import loc.filter.filters.TimeModified.ModifiedEarlier;
import loc.filter.filters.TimeModified.ModifiedLater;

import java.util.HashMap;

public class FilterFactory {
	private static final HashMap<Character, Class> serializers;
	static {
		serializers = new HashMap<>();
		serializers.put(Not.prefix,                 Not.Serializer.class);
		serializers.put(And.prefix,                 And.Serializer.class);
		serializers.put(Or.prefix,                  Or.Serializer.class);
		serializers.put(FileExtensionFilter.prefix, FileExtensionFilter.Serializer.class);
		serializers.put(ModifiedEarlier.prefix,     ModifiedEarlier.Serializer.class);
		serializers.put(ModifiedLater.prefix,       ModifiedLater.Serializer.class);
	}

	public static Filter create(String filterString) throws FilterSerializeException {
		try {
			filterString = filterString.trim();
			char prefix = filterString.charAt(0);
			FilterSerializer parser = (FilterSerializer) serializers.get(prefix).newInstance();
			return parser.serialize(filterString);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new FilterCreateException(e);
		}
	}
}
