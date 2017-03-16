package loc.filter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	public static final char __openParenthesis  = '(';
	public static final char __closeParenthesis = ')';
	public static final char __separator        = ' ';

	private int parenthesisCounter;
	private int currentChar;
	private StringBuilder filterStringBuffer;
	private BufferedInputStream reader;
	private List<Filter> filterList;
	private String filterString;

	public static class ParseException extends FilterSerializeException {
		public ParseException(String message) {
			super(message);
		}

		public ParseException(Throwable cause) {
			super(cause);
		}
	}

	public Parser(String filterString) {
		this.filterString = filterString;
		filterStringBuffer = new StringBuilder();
	}

	private void initReader() {
		reader = new BufferedInputStream(new ByteArrayInputStream(filterString.getBytes()));
	}

	public Parser clearBuffer() {
		filterStringBuffer.delete(0, filterStringBuffer.length());
		return this;
	}

	private void initFilterList() {
		filterList = new ArrayList<>();
	}

	public Parser openParenthesis() throws ParseException {
		try {
			currentChar = reader.read();
			if (currentChar != __openParenthesis) {
				throw new ParseException("Couldn't open parenthesis: first character is not '" + __openParenthesis + '\'');
			}
			parenthesisCounter = 1;
			currentChar = reader.read();
			while (currentChar != -1) {
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
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	static public String openParenthesis(String filterString) throws ParseException {
		int open = filterString.indexOf(__openParenthesis);
		int close = filterString.lastIndexOf(__closeParenthesis);
		if (open == -1 || open > close) throw new ParseException("Couldn't open parenthesis in \"" + filterString + "\"");
		return filterString.substring(open + 1, close);
	}

	public Filter[] parseSequence() throws FilterSerializeException {
		try {
			initFilterList();
			initReader();
			clearBuffer();
			parenthesisCounter = 0;
			currentChar = reader.read();
			skipSpaces();
			while (currentChar != -1) {
				if (currentChar == __openParenthesis) {
					++parenthesisCounter;
				} else if (currentChar == __closeParenthesis) {
					--parenthesisCounter;
				} else if (currentChar == __separator && parenthesisCounter == 0) {
					addFilter();
					clearBuffer();
					skipSpaces();
				}
				filterStringBuffer.append((char) currentChar);
				currentChar = reader.read();
			}
			if (parenthesisCounter != 0) {
				throw new ParseException(
						"Wrong parenthesis count " + parenthesisCounter +
								" in \"" + filterString + '"');
			}
			if (filterStringBuffer.length() > 0) addFilter();
			if (filterList.size() == 0) throw new ParseException("Empty filter sequence");
			return filterList.toArray(new Filter[filterList.size()]);
		} catch (FilterSerializeException e) {
			throw e;
		} catch (IOException e) {
			throw new FilterSerializeException(e);
		}
	}

	private void addFilter() throws FilterSerializeException {
		String filterString = filterStringBuffer.toString();
		Filter filter = FilterFactory.create(filterString);
		filterList.add(filter);
	}

	public String readToTheEnd() throws ParseException {
		try {
			while ((currentChar = reader.read()) != -1) {
				filterStringBuffer.append((char) currentChar);
			}
			return filterStringBuffer.toString();
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	public Parser skipCharIfExists(char character) throws ParseException {
		try {
			reader.mark(1);
			if (reader.read() != character) {
				reader.reset();
			}
			return this;
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	public Parser skipChar(char character) throws ParseException {
		try {
			if ((char) reader.read() != character) {
				throw new ParseException("Couldn't skip '" + character + '\'');
			}
			return this;
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	public Parser skipChars(char character, int amount) throws ParseException {
		if (amount == 0) return this;
		try {
			int count = 0;
			do {
				char readChar = (char) reader.read();
				if (readChar != character) {
					throw new ParseException("Couldn't skip " + amount + " '" + character + '\'');
				}
			} while (++count < amount);
			return this;
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	public Parser skipAtMostChars(char character, int amount) throws ParseException {
		if (amount == 0) return this;
		try {
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
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	public Parser skipSpaces() throws ParseException {
		try {
			while (Character.isSpaceChar(currentChar)) {
				currentChar = reader.read();
			}
			return this;
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	public String getCurrentBufferString() {
		return filterStringBuffer.toString();
	}
}
