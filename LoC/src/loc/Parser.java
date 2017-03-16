package loc;

import loc.filter.*;
import loc.filter.FilterFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class FilterParser {
	public static final char __openParenthesis  = '(';
	public static final char __closeParenthesis = ')';
	public static final char __separator        = ' ';

	private int parenthesisCounter;
	private int currentChar;
	private StringBuffer filterStringBuffer;
	private BufferedInputStream reader;
	private List<IFilter> filterList;
	private String filterString;

	public static class FilterParseException extends Exception {
		public FilterParseException(String message) {
			super(message);
		}
	}

	public FilterParser(String filterString) {
		this.filterString = filterString;
		filterStringBuffer = new StringBuffer();
	}

	private void initReader() {
		reader = new BufferedInputStream(new ByteArrayInputStream(filterString.getBytes()));
	}

	public FilterParser clearBuffer() {
		filterStringBuffer.delete(0, filterStringBuffer.length());
		return this;
	}

	private void initFilterList() {
		filterList = new ArrayList<>();
	}

	public FilterParser openParenthesis() throws FilterParseException, IOException {
		currentChar = reader.read();
		if (currentChar != __openParenthesis) {
			throw new FilterParseException("Couldn't open parenthesis: first character is not '" + __openParenthesis + '\'');
		}
		parenthesisCounter = 1;
		currentChar = reader.read();
		while(currentChar != -1) {
			if (currentChar == __openParenthesis) {
				++parenthesisCounter;
			} else if (currentChar == __closeParenthesis) {
				--parenthesisCounter;
			}
			if (parenthesisCounter > 0) {
				filterStringBuffer.append((char) currentChar);
			} else if (parenthesisCounter == 0) {
				break;
			}
			currentChar = reader.read();
		}
		return this;
	}

	static public String openParenthesis(String filterString) throws FilterParseException {
		int open = filterString.indexOf(__openParenthesis);
		int close = filterString.lastIndexOf(__closeParenthesis);
		if (open == -1 || open > close) throw new FilterParseException("Couldn't open parenthesis in \"" + filterString + "\"");
		return filterString.substring(open + 1, close);
	}

	public IFilter[] parseSequence() throws
			FilterParseException,
			IOException,
			IllegalAccessException,
			NoSuchMethodException,
			InvocationTargetException {
		initFilterList();
		initReader();
		clearBuffer();
		parenthesisCounter = 0;
		currentChar = reader.read();
		skipSpaces();
		while(currentChar != -1) {
			if (currentChar == __openParenthesis) {
				++parenthesisCounter;
			} else if (currentChar == __closeParenthesis) {
				--parenthesisCounter;
			} else if (currentChar == __separator && parenthesisCounter == 0) {
				addFilter();
				filterStringBuffer = new StringBuffer(); // clear buffer
				skipSpaces();
			}
			filterStringBuffer.append((char) currentChar);
			currentChar = reader.read();
		}
		if (parenthesisCounter != 0) {
			throw new FilterParseException(
					"Wrong parenthesis count " + parenthesisCounter +
					" in \"" + filterString + '"');
		}
		if (filterStringBuffer.length() > 0) addFilter();
		if (filterList.size() == 0) throw new FilterParseException("Empty filter sequence");
		return filterList.toArray(new IFilter[filterList.size()]);
	}

	private void addFilter() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		String filterString = filterStringBuffer.toString();
		IFilter filter = FilterFactory.create(filterString);
		filterList.add(filter);
	}

	public FilterParser skipCharIfExists(char character) throws IOException {
		reader.mark(1);
		if (reader.read() != character) {
			reader.reset();
		}
		return this;
	}

	public FilterParser skipChar(char character) throws IOException, FilterParseException {
		char readChar = (char) reader.read();
		if (readChar != character) {
			throw new FilterParseException("Couldn't skip '" + character + '\'');
		}
		return this;
	}

	public FilterParser skipChars(char character, int amount) throws IOException, FilterParseException {
		if (amount == 0) return this;
		int count = 0;
		do {
			char readChar = (char) reader.read();
			if (readChar != character) {
				throw new FilterParseException("Couldn't skip " + amount + " '" + character + '\'');
			}
		} while (++count < amount);
		return this;
	}

	public FilterParser skipAtMostChars(char character, int amount) throws IOException, FilterParseException {
		if (amount == 0) return this;
		int count = 0;
		do {
			reader.mark(1);
			char readChar = (char) reader.read();
			if (readChar != character) {
				reader.reset();
				break;
			}
		} while (++count < amount);
		return this;
	}

	public FilterParser skipSpaces() throws IOException {
		while (Character.isSpaceChar(currentChar)) {
			currentChar = reader.read();
		}
		return this;
	}

	public String getCurrentBufferString() {
		String result = filterStringBuffer.toString();
		return result;
	}
}
