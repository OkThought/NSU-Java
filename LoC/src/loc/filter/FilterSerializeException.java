package loc.filter;

import java.io.IOException;

public class FilterSerializeException extends IOException {
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
