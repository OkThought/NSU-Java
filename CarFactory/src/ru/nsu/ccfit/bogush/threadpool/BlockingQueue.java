package ru.nsu.ccfit.bogush.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BlockingQueue<T> {
	private final Queue<T> queue = new LinkedList<>();
	private final List<SizeSubscriber> sizeSubscribers = new ArrayList<>();

	private final int capacity;
	private final Object lock = new Object();

	private static final String LOGGER_NAME = "BlockingQueue";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public BlockingQueue() {
		this(Integer.MAX_VALUE);
	}

	public BlockingQueue(int capacity) {
		logger.traceEntry();
		logger.trace("initialize BlockingQueue of capacity " + capacity);
		this.capacity = capacity;
		logger.traceExit();
	}

	public boolean isFull() {
		logger.traceEntry();
		return logger.traceExit(queue.size() == capacity);
	}

	public boolean isEmpty() {
		logger.traceEntry();
		return queue.isEmpty();
	}

	public void put(T t) throws InterruptedException {
		logger.traceEntry();
		synchronized (lock) {
			while (isFull()) {
				lock.wait();
			}
			queue.add(t);
			logger.trace(t + " was put");
			sizeChanged(size());
			lock.notifyAll();
		}
		logger.traceExit();
	}

	public T take() throws InterruptedException {
		logger.traceEntry();
		T result;
		synchronized (lock) {
			while (isEmpty()) {
				lock.wait();
			}
			result = queue.remove();
			logger.trace(result + " taken");
			sizeChanged(size());
			lock.notifyAll();
		}
		return logger.traceExit(result);
	}

	public int size() {
		logger.traceEntry();
		return logger.traceExit(queue.size());
	}

	public int getCapacity() {
		logger.traceEntry();
		return logger.traceExit(capacity);
	}

	public synchronized void addSizeSubscriber(SizeSubscriber sizeSubscriber) {
		logger.traceEntry();
		logger.trace("add SizeSubscriber " + sizeSubscriber.getClass().getSimpleName());
		sizeSubscribers.add(sizeSubscriber);
		logger.traceExit();
	}

	private void sizeChanged(int size) {
		logger.traceEntry();
		for (SizeSubscriber sizeSubscriber: sizeSubscribers) {
			logger.trace("send new size (" + size + ") to subscriber " + sizeSubscriber.getClass().getSimpleName());
			sizeSubscriber.sizeChanged(size);
		}
		logger.traceExit();
	}

	public interface SizeSubscriber {
		void sizeChanged(int size);
	}
}
