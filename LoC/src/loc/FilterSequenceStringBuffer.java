package loc;

import loc.filter.Filter;

import java.io.*;

public class FilterSequenceStringBuffer {
	public static final char __openParenthesis  = '(';
	public static final char __closeParenthesis = ')';
	public static final char __separator        = ' ';

	private StringBuilder buffer;

	public FilterSequenceStringBuffer() {
		this.buffer = new StringBuilder();
	}

	public FilterSequenceStringBuffer append(Filter[] filters) throws Exception {
		if (filters.length == 0) throw new IOException("Can't print sequence of 0 filter");
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
