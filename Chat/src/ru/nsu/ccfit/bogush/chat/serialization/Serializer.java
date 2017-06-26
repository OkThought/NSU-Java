package ru.nsu.ccfit.bogush.chat.serialization;

public interface Serializer<T> {
	void serialize(T obj) throws SerializerException;
	T deserialize() throws SerializerException;
	Type getType();

	class SerializerException extends Exception {
		public SerializerException() {
			super();
		}

		public SerializerException(String message) {
			super(message);
		}

		public SerializerException(String message, Throwable cause) {
			super(message, cause);
		}

		public SerializerException(Throwable cause) {
			super(cause);
		}

		protected SerializerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}

	class Type {
		public static final String OBJ = "obj";
		public static final String XML = "xml";
		public static final Type OBJ_SERIALIZER = new Type(OBJ);
		public static final Type XML_SERIALIZER = new Type(XML);

		private final String type;

		public Type(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

		@Override
		public String toString() {
			return type;
		}
	}
}
