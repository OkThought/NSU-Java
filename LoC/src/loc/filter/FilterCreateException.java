package loc.filter;

public class FilterCreateException extends FilterSerializeException {
	public FilterCreateException(String message) {
		super(message);
	}

	public FilterCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilterCreateException(Throwable cause) {
		super(cause);
	}
}
