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
		logger.trace("initialize Storage<" + contentType.getSimpleName() + "> with capacity " + capacity);
		this.contentType = contentType;
		this.capacity = capacity;
		this.queue = new BlockingQueue<>(capacity);
	}

	public void store(T thing) throws InterruptedException {
		logger.trace("store " + thing);
		queue.put(thing);
		logger.trace(thing + " stored in " + this);
	}

	public T take() throws InterruptedException {
		logger.trace("take " + contentType.getSimpleName());
		T result = queue.take();
		logger.trace(result + " taken from " + this);
		return result;
	}

	public synchronized void addSizeSubscriber(BlockingQueue.SizeSubscriber sizeSubscriber) {
		queue.addSizeSubscriber(sizeSubscriber);
	}

	public int size() {
		return queue.size();
	}

	public int capacity() { return capacity; }

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public boolean isFull() {
		return queue.isFull();
	}

	@Override
	public String toString() {
		return  getClass().getSimpleName() + "<" + contentType.getSimpleName() +
				">(size=" + queue.size() +
				" capacity=" + capacity + ")";
	}
}
