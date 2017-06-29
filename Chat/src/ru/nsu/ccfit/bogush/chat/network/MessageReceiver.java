package ru.nsu.ccfit.bogush.chat.network;

import ru.nsu.ccfit.bogush.chat.message.Message;

public interface MessageReceiver {
	Message receiveMessage() throws Exception;

	class Exception extends java.lang.Exception {
		public Exception() {
			super();
		}

		public Exception(String message) {
			super(message);
		}

		public Exception(String message, Throwable cause) {
			super(message, cause);
		}

		public Exception(Throwable cause) {
			super(cause);
		}

		protected Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}
}
