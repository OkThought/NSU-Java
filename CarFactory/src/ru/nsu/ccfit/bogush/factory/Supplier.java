package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Supplier<T extends CarFactoryObject> extends SimplePeriodical implements Runnable {
	private Storage<T> storage;
	private static final long DEFAULT_PERIOD = 0;
	private final Class<T> contentType;
	private final Thread thread;

	private static final String LOGGER_NAME = "Supplier";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public Supplier(Storage<T> storage, Class<T> contentType) {
		this(storage, contentType, DEFAULT_PERIOD);
	}

	public Supplier(Storage<T> storage, Class<T> contentType, long period) {
		super(period);
		logger.traceEntry();
		logger.trace("initialize with period " + period);
		this.storage = storage;
		this.contentType = contentType;
		this.thread = new Thread(this);
		thread.setName(toString());
		logger.traceExit();
	}

	@Override
	public void run() {
		logger.traceEntry();
		try {
			while (!Thread.interrupted()) {
				storage.store(contentType.newInstance());
				waitPeriod();
			}
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			logger.trace("interrupted");
		} finally {
			logger.trace("stopped");
			logger.traceExit();
		}
	}

	@Override
	public String toString() {
		return contentType.getSimpleName() + "-" + getClass().getSimpleName() + "-" + thread.getId();
	}

	public Thread getThread() {
		logger.traceEntry();
		return logger.traceExit(thread);
	}
}
