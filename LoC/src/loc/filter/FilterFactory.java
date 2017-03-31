package loc.filter;

import com.sun.javaws.exceptions.InvalidArgumentException;
import loc.filter.filters.aggregate.And;
import loc.filter.filters.aggregate.Not;
import loc.filter.filters.aggregate.Or;
import loc.filter.filters.FileExtensionFilter;
import loc.filter.filters.time.ModifiedEarlier;
import loc.filter.filters.time.ModifiedLater;
import loc.filter.serializers.FileExtensionFilterSerializer;
import loc.filter.serializers.aggregate.AndFilterSerializer;
import loc.filter.serializers.aggregate.NotFilterSerializer;
import loc.filter.serializers.aggregate.OrFilterSerializer;
import loc.filter.serializers.time.ModifiedEarlierFilterSerializer;
import loc.filter.serializers.time.ModifiedLaterFilterSerializer;

import java.util.HashMap;

public class FilterFactory {
	private final HashMap<Character, FilterSerializer> serializers;
	private final HashMap<Class, Character> filterPrefixes;
	private static FilterFactory factory = null;

	private FilterFactory() {
		factory = this;
		serializers = new HashMap<>();
		serializers.put(NotFilterSerializer.prefix,                 NotFilterSerializer.getInstance());
		serializers.put(AndFilterSerializer.prefix,                 AndFilterSerializer.getInstance());
		serializers.put(OrFilterSerializer.prefix,                  OrFilterSerializer.getInstance());
		serializers.put(FileExtensionFilterSerializer.prefix,       FileExtensionFilterSerializer.getInstance());
		serializers.put(ModifiedEarlierFilterSerializer.prefix,     ModifiedEarlierFilterSerializer.getInstance());
		serializers.put(ModifiedLaterFilterSerializer.prefix,       ModifiedLaterFilterSerializer.getInstance());

		filterPrefixes = new HashMap<>();
		filterPrefixes.put(Not.class, NotFilterSerializer.prefix);
		filterPrefixes.put(And.class, AndFilterSerializer.prefix);
		filterPrefixes.put(Or.class, OrFilterSerializer.prefix);
		filterPrefixes.put(ModifiedLater.class, ModifiedLaterFilterSerializer.prefix);
		filterPrefixes.put(ModifiedEarlier.class, ModifiedEarlierFilterSerializer.prefix);
		filterPrefixes.put(FileExtensionFilter.class, FileExtensionFilterSerializer.prefix);
	}

	private Filter createByString(String filterString) throws FilterSerializeException {
		filterString = filterString.trim();
		char prefix = filterString.charAt(0);
		FilterSerializer serializer = serializers.get(prefix);
		return serializer.serialize(filterString);
	}

	public static Filter create(String filterString) throws FilterSerializeException {
		if (filterString == null) throw new NullPointerException("filterString is null");
		filterString = filterString.trim();
		if (filterString.isEmpty()) throw new FilterSerializeException("filterString is empty");
		return getInstance().createByString(filterString);
	}

	public static FilterSerializer getSerializer(char prefix) {
		return getInstance().serializers.get(prefix);
	}

	public static FilterSerializer getSerializer(Filter filter) {
		char prefix = getInstance().filterPrefixes.get(filter.getClass());
		return getInstance().serializers.get(prefix);
	}

	private static synchronized FilterFactory getInstance() {
		if (factory == null) {
			factory = new FilterFactory();
		}
		return factory;
	}
}
