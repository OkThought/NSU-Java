package ru.nsu.ccfit.bogush.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BlockingQueue<T> {
	private Queue<T> queue = new LinkedList<T>();
	private List<SizeSubscriber> sizeSubscribers = new ArrayList<>();

	private final int capacity;
	private final Object sync = new Object();

	private static final String LOGGER_NAME = "BlockingQueue";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public BlockingQueue() {
		this(Integer.MAX_VALUE);
	}

	public BlockingQueue(int capacity) {
		logger.trace("initialize BlockingQueue of capacity " + capacity);
		this.capacity = capacity;
	}

	public boolean isFull() {
		return queue.size() == capacity;
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public void put(T t) throws InterruptedException {
		synchronized (sync) {
			while (isFull()) {
				sync.wait();
			}
			queue.add(t);
			sizeChanged(size());
			sync.notifyAll();
		}
	}

	public T take() throws InterruptedException {
		T result;
		synchronized (sync) {
			while (isEmpty()) {
				sync.wait();
			}
			result = queue.remove();
			sizeChanged(size());
			sync.notifyAll();
		}
		return result;
	}

	public int size() {
		return queue.size();
	}

	public int getCapacity() {
		return capacity;
	}

	public void subscribe(SizeSubscriber sizeSubscriber) {

		sizeSubscribers.add(sizeSubscriber);
	}

	private void sizeChanged(int size) {
		for (SizeSubscriber sizeSubscriber: sizeSubscribers) {
			sizeSubscriber.sizeChanged(size);
		}
	}

	public static interface SizeSubscriber {
		void sizeChanged(int size);
	}
}
