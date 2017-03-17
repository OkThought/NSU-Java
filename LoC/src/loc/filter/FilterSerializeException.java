package loc.filter;

import loc.SerializeException;

public class FilterSerializeException extends SerializeException {
	public FilterSerializeException(String message) {
		super(message);
	}

	public FilterSerializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilterSerializeException(Throwable cause) {
		super(cause);
	}
}
