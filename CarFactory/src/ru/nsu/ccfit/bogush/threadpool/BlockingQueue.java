package ru.nsu.ccfit.bogush.threadpool;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue<T> {
	private Queue<T> queue = new LinkedList<T>();
	private final int size;
	private final Object sync = new Object();

	public BlockingQueue() {
		size = Integer.MAX_VALUE;
	}

	public BlockingQueue(int size) {
		this.size = size;
	}

	public boolean isFull() {
		return queue.size() == size;
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
			sync.notifyAll();
		}
		return result;
	}

	public int size() {
		return queue.size();
	}
}
