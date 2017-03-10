package loc;

import loc.filters.*;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class FilterParser {
	public static final char __openParenthesis  = '(';
	public static final char __closeParenthesis = ')';
	public static final char __separator        = ' ';

	public static class FilterParseException extends Exception {
		public FilterParseException(String message) {
			super(message);
		}
	}

	public static FilterNot parseNot(String filterString) throws
			FilterParseException,
			IllegalAccessException,
			NoSuchMethodException,
			InvocationTargetException {
		filterString = filterString.trim();
		if (filterString.charAt(0) == __openParenthesis) {
			filterString = openParenthesis(filterString);
		}
		Filter filter = FilterFactory.create(filterString);
		return new FilterNot(filter);
	}

	public static FilterAnd parseAnd(String filterString) throws
			FilterParseException,
			InvocationTargetException,
			NoSuchMethodException,
			IllegalAccessException,
			IOException {
		filterString = openParenthesis(filterString);
		return new FilterAnd(parseSequence(filterString));
	}

	public static FilterOr parseOr(String filterString) throws
			FilterParseException,
			InvocationTargetException,
			NoSuchMethodException,
			IllegalAccessException,
			IOException {
		filterString = openParenthesis(filterString);
		return new FilterOr(parseSequence(filterString));
	}

	public static FileExtensionFilter parseFileExtension(String filterString) {
		return new FileExtensionFilter(filterString);
	}

	public static ModifiedEarlierFilter parseTimeLess(String filterString) {
		return new ModifiedEarlierFilter(Long.parseLong(filterString));
	}

	public static ModifiedLaterFilter parseTimeGreater(String filterString) {
		return new ModifiedLaterFilter(Long.parseLong(filterString));
	}

	private static String openParenthesis(String filterString) throws FilterParseException {
		int open = filterString.indexOf(__openParenthesis);
		int close = filterString.lastIndexOf(__closeParenthesis);
		if (open == -1 || open > close) throw new FilterParseException("Couldn't open parenthesis in \"" + filterString + "\"");
		return filterString.substring(open + 1, close);
	}

	private static Filter[] parseSequence(String filtersString) throws
			FilterParseException,
			IOException,
			IllegalAccessException,
			NoSuchMethodException,
			InvocationTargetException {
		List<Filter> filters = new ArrayList<>();
		StringReader reader = new StringReader(filtersString);
		int parenthesisCounter = 0;
		int currentIndex = 0;
		int filterBegin = 0;
		int filterEnd;
		int currentChar;
		while(true) {
			currentChar = reader.read();
			if (currentChar == __openParenthesis) {
				++parenthesisCounter;
			} else if (currentChar == __closeParenthesis) {
				--parenthesisCounter;
			} else if ((currentChar == __separator && parenthesisCounter == 0) || currentChar == -1) {
				filterEnd = currentIndex;
				String filterString = filtersString.substring(filterBegin, filterEnd);
				Filter filter = FilterFactory.create(filterString);
				filters.add(filter);
				filterBegin = currentIndex + 1;
				if (currentChar == -1) break;
			}
			currentIndex++;
		}
		if (filters.size() == 0) throw new FilterParseException("Empty filter sequence");
		return filters.toArray(new Filter[filters.size()]);
	}
}
