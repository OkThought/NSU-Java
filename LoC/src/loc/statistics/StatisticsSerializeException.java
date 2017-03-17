package loc.statistics;

import loc.SerializeException;

public class StatisticsSerializeException extends SerializeException {
	public StatisticsSerializeException(String message) {
		super(message);
	}

	public StatisticsSerializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public StatisticsSerializeException(Throwable cause) {
		super(cause);
	}
}
