package loc;

import loc.filters.*;

/**
 * Created by Ivan on 3/9/17.
 */
public interface IFilterSerializer {
	public static class FilterSerializerException extends Exception {
		public FilterSerializerException(String message) {
			super(message);
		}
	}

	String serialize(FilterAnd filter) throws FilterSerializerException;
	String serialize(FilterOr filter) throws FilterSerializerException;
	String serialize(FilterNot filter) throws FilterSerializerException;
	String serialize(ModifiedLaterFilter filter);
	String serialize(ModifiedEarlierFilter filter);
	String serialize(FileExtensionFilter filter);
	String serialize(Filter[] filters) throws FilterSerializerException;

	String serialize(Filter filter) throws FilterSerializerException;
}
