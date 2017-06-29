package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.threadpool.BlockingQueue;


public class Storage <T extends CarFactoryObject> {
	private final BlockingQueue<T> queue;
	private final int capacity;
	private final Class<T> contentType;

	private static final String LOGGER_NAME = "Storage";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public Storage(Class<T> contentType, int capacity) {
		logger.traceEntry();
		logger.trace("initialize Storage<" + contentType.getSimpleName() + "> with capacity " + capacity);
		this.contentType = contentType;
		this.capacity = capacity;
		this.queue = new BlockingQueue<>(capacity);
		logger.traceExit();
	}

	public void store(T thing) throws InterruptedException {
		logger.traceEntry();
		logger.trace("store " + thing);
		queue.put(thing);
		logger.trace(thing + " stored in " + this);
		logger.traceExit();
	}

	public T take() throws InterruptedException {
		logger.traceEntry();
		logger.trace("take " + contentType.getSimpleName());
		T result = queue.take();
		logger.trace(result + " taken from " + this);
		return logger.traceExit(result);
	}

	public synchronized void addSizeSubscriber(BlockingQueue.SizeSubscriber sizeSubscriber) {
		logger.traceEntry();
		queue.addSizeSubscriber(sizeSubscriber);
		logger.traceExit();
	}

	public int size() {
		logger.traceEntry();
		return logger.traceExit(queue.size());
	}

	public int capacity() {
		logger.traceEntry();
		return logger.traceExit(capacity);
	}

	public boolean isEmpty() {
		logger.traceEntry();
		return logger.traceExit(queue.isEmpty());
	}

	public boolean isFull() {
		logger.traceEntry();
		return logger.traceExit(queue.isFull());
	}

	@Override
	public String toString() {
		return  getClass().getSimpleName() + "<" + contentType.getSimpleName() +
				">(size=" + queue.size() +
				" capacity=" + capacity + ")";
	}
}
