package loc.filter;

public class FilterStringStream {
	public static final char __openParenthesis  = '(';
	public static final char __closeParenthesis = ')';
	public static final char __separator        = ' ';

	private StringBuilder buffer;
	private final StringBuilder filterBuffer;
	private final int length;
	private int __position = 0;
	private char __current;
	private boolean __endReached;

	public FilterStringStream() {
		this.buffer = new StringBuilder();
		this.filterBuffer = buffer;
		length = filterBuffer.length();
		__endReached = length == 0;
		if (!__endReached) __current = filterBuffer.charAt(0);
	}

	public FilterStringStream(String filterString) {
		this.buffer = new StringBuilder();
		this.filterBuffer = new StringBuilder(filterString);
		length = filterBuffer.length();
		__endReached = length == 0;
		if (!__endReached) __current = filterBuffer.charAt(0);
	}

	private boolean increment() {
		if (__endReached) return false;
		__endReached = ++__position == length;
		if (!__endReached) {
			__current = filterBuffer.charAt(__position);
		}
		return true;
	}

	private boolean decrement() {
		if (__position <= 0) return false;
		__current = filterBuffer.charAt(--__position);
		__endReached = false;
		return true;
	}

	public boolean endReached() {
		return __endReached;
	}

	public FilterStringStream clearBuffer() {
		buffer.delete(0, buffer.length());
		return this;
	}

	public char getCurrent() {
		return __current;
	}

	public int getPosition() {
		return __position;
	}

	public FilterStringStream append(Filter[] filters) throws FilterSerializeException {
		if (filters.length == 0) throw new FilterSerializeException("Can't print sequence of 0 filter");
		boolean first = true;
		buffer.append(__openParenthesis);
		for (Filter filter : filters) {
			if (first) {
				first = false;
			} else {
				buffer.append(__separator);
			}
			buffer.append(FilterFactory.getSerializer(filter).serialize(filter));
		}
		buffer.append(__closeParenthesis);
		return this;
	}

	public FilterStringStream append(char c) {
		buffer.append(c);
		return this;
	}

	public FilterStringStream append(char c, int count) {
		for (int i = 0; i < count; ++i) {
			buffer.append(c);
		}
		return this;
	}

	public FilterStringStream skip(char c) throws FilterSerializeException {
		if (__endReached) throw new FilterSerializeException("Reached the end of buffer");
		if (__current != c) throw new FilterSerializeException("Can't skip '" + c + "\' found '" + __current + '\'');
		increment();
		return this;
	}

	public FilterStringStream skip(char c, int count) throws FilterSerializeException {
		for (int i = 0; i < count; i++) {
			skip(c);
		}
		return this;
	}

	public FilterStringStream skip() throws FilterSerializeException {
		if (!increment()) throw new FilterSerializeException("Reached the end of buffer");
		return this;
	}

	public FilterStringStream skip(int count) throws FilterSerializeException {
		for (int i = 0; i < count; i++) {
			skip();
		}
		return this;
	}

	public FilterStringStream skipAll(char c) {
		if (__current != c) return this;
		while (increment()) {
			if (__current != c) {
				break;
			}
		}
		return this;
	}

	public FilterStringStream skipWhitespace() throws FilterSerializeException {
		if (__endReached) throw new FilterSerializeException("Reached the end of buffer");
		if (!Character.isSpaceChar(__current)) throw new FilterSerializeException("Can't skip whitespace found '" + __current + '\'');
		increment();
		return this;
	}

	public FilterStringStream skipWhitespaces() {
		while (Character.isSpaceChar(__current)) {
			if (!increment() || !Character.isSpaceChar(__current)) {
				break;
			}
		}
		return this;
	}

	public int readChar() {
		if (!__endReached) {
			char result = __current;
			increment();
			return result;
		}
		else return -1;
	}

	public String readWord() throws FilterSerializeException {
		// [ ][(]word[)]
		skipWhitespaces();
		int parenthesisCounter = 0;
		if (__endReached) {
			throw new FilterSerializeException("Reached the end of buffer");
		}
		while (!__endReached) {
			if (__current == __openParenthesis) {
				parenthesisCounter++;
			} else if (__current == __closeParenthesis) {
				parenthesisCounter--;
			} else if (parenthesisCounter == 0 && Character.isSpaceChar(__current)){
				break;
			}
			append(__current);
			increment();
		}
		return toString();
	}

	public String readAll() {
		return buffer.toString() + filterBuffer.substring(__position);
	}

	public FilterStringStream openParenthesis(char openParenthesis, char closeParenthesis) throws FilterSerializeException {
		if (__endReached) {
			throw new FilterSerializeException("Reached the end of buffer");
		}
		if (__current != openParenthesis) {
			throw new FilterSerializeException("Couldn't open parenthesis: first character is not '" + closeParenthesis + '\'');
		}
		int parenthesisCounter = 1;
		while (increment()) {
			if (__current == openParenthesis) {
				++parenthesisCounter;
			} else if (__current == closeParenthesis) {
				--parenthesisCounter;
			}
			if (parenthesisCounter > 0) {
				append(__current);
			} else if (parenthesisCounter == 0) {
				return this;
			}
		}
		throw new FilterSerializeException("Bad parenthesis count in \"" + filterBuffer.toString());
	}

	public FilterStringStream openParenthesis() throws FilterSerializeException {
		return openParenthesis(__openParenthesis, __closeParenthesis);
	}

	@Override
	public String toString() {
		return buffer.toString();
	}
}
