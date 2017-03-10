package loc;

import loc.filters.*;

import java.util.ArrayList;
import java.util.List;

public class FilterSerializer implements IFilterSerializer {
	public static String delimiter        = " ";
	public static String beginSequence    = "(";
	public static String endSequence      = ")";
	public static String and              = Character.toString(FilterAnd.prefix);
	public static String or               = Character.toString(FilterOr.prefix);
	public static String not              = Character.toString(FilterNot.prefix);
	public static String timeGreater      = Character.toString(ModifiedLaterFilter.prefix);
	public static String timeLess         = Character.toString(ModifiedEarlierFilter.prefix);
	public static String extensionPoint   = Character.toString(FileExtensionFilter.prefix);

	@Override
	public String serialize(FilterAnd filter) throws FilterSerializerException {
		return and + beginSequence + serialize(filter.filters) + endSequence;
	}

	@Override
	public String serialize(FilterOr filter) throws FilterSerializerException {
		return or + beginSequence + serialize(filter.filters) + endSequence;
	}

	@Override
	public String serialize(FilterNot filter) throws FilterSerializerException {
		return not + serialize(filter.filter);
	}

	@Override
	public String serialize(ModifiedLaterFilter filter) {
		return timeGreater + filter.timeBound;
	}

	@Override
	public String serialize(ModifiedEarlierFilter filter) {
		return timeLess + filter.timeBound;
	}

	@Override
	public String serialize(FileExtensionFilter filter) {
		return extensionPoint + filter.extension;
	}

	@Override
	public String serialize(Filter[] filters) throws FilterSerializerException {
		List<String> stringList = new ArrayList<>();
		for (Filter filter: filters) {
			stringList.add(serialize(filter));
		}
		return String.join(delimiter, stringList);
	}

	@Override
	public String serialize(Filter filter) throws FilterSerializerException {
		if (filter instanceof FileExtensionFilter) {
			return serialize((FileExtensionFilter) filter);
		} else if (filter instanceof ModifiedLaterFilter) {
			return serialize((ModifiedLaterFilter) filter);
		} else if (filter instanceof ModifiedEarlierFilter) {
			return serialize((ModifiedEarlierFilter) filter);
		} else if (filter instanceof FilterAnd) {
			return serialize((FilterAnd) filter);
		} else if (filter instanceof FilterOr) {
			return serialize((FilterOr) filter);
		} else if (filter instanceof FilterNot) {
			return serialize((FilterNot) filter);
		} else {
			throw new FilterSerializerException("Wrong type of filter: " + filter.getClass());
		}
	}
}
