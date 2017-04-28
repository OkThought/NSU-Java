package ru.nsu.ccfit.bogush.factory.storage;

import ru.nsu.ccfit.bogush.factory.thing.Thing;
import ru.nsu.ccfit.bogush.threadpool.BlockingQueue;

import java.util.LinkedList;

public class Storage <T extends Thing> {
	private BlockingQueue<T> queue;
	private final int capacity;
	private Class<T> type;
	private LinkedList<Subscriber> subscribers = new LinkedList<>();

	public Storage(Class<T> type, int capacity) {
		this.type = type;
		this.capacity = capacity;
		this.queue = new BlockingQueue<T>(capacity);
	}

	public void store(T thing) throws InterruptedException {
		queue.put(thing);
		sizeChanged(size());
	}

	public T take() throws InterruptedException {
		T result = queue.take();
		sizeChanged(size());
		return result;
	}

	private void sizeChanged(int size) {
		for (Subscriber subscriber: subscribers) {
			subscriber.sizeChanged(size);
		}
	}

	public void subscribe(Subscriber subscriber) {
		subscribers.add(subscriber);
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
		return "Storage<" + type.getSimpleName() + ">(capacity=" + queue.size() + " maxsize=" + capacity + ")";
	}

	public static interface Subscriber {
		void sizeChanged(int size);
	}
}
