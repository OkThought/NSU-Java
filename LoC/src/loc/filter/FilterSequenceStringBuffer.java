package loc.filter;

import java.io.*;

public class FilterSequenceStringBuffer {
	public static final char __openParenthesis  = '(';
	public static final char __closeParenthesis = ')';
	public static final char __separator        = ' ';

	private StringBuilder buffer;

	public FilterSequenceStringBuffer() {
		this.buffer = new StringBuilder();
	}

	public FilterSequenceStringBuffer append(Filter[] filters) throws FilterSerializeException {
		if (filters.length == 0) throw new FilterSerializeException("Can't print sequence of 0 filter");
		boolean first = true;
		buffer.append(__openParenthesis);
		for (Filter filter : filters) {
			if (first) {
				first = false;
			} else {
				buffer.append(__separator);
			}
			buffer.append(filter.getSerializer().serialize(filter));
		}
		buffer.append(__closeParenthesis);
		return this;
	}

	@Override
	public String toString() {
		return buffer.toString();
	}
}
