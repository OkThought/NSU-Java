package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Supplier<T extends CarFactoryObject> extends SimplePeriodical implements Runnable {
	private Storage<T> storage;
	private static final long DEFAULT_PERIOD = 0;
	private Class<T> contentType;
	private Thread thread;

	private static final String LOGGER_NAME = "Supplier";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public Supplier(Storage<T> storage, Class<T> contentType) {
		this(storage, contentType, DEFAULT_PERIOD);
	}

	public Supplier(Storage<T> storage, Class<T> contentType, long period) {
		super(period);
		logger.trace("initialize with period " + period);
		this.storage = storage;
		this.contentType = contentType;
		this.thread = new Thread(this);
		thread.setName(toString());
	}

	@Override
	public void run() {
		try {
			while (true) {
				storage.store(contentType.newInstance());
				waitPeriod();
			}
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			logger.trace("interrupted");
		} finally {
			logger.trace("stopped");
		}
	}

	@Override
	public String toString() {
		return contentType.getSimpleName() + "-" + getClass().getSimpleName() + "-" + thread.getId();
	}

	public Thread getThread() {
		return thread;
	}
}
