package ru.nsu.ccfit.bogush.chat.server;

import java.util.ArrayList;
import java.util.Arrays;

public class ConcurrentRollingArray<E> {
	private Object[] elements;
	private final int capacity;
	private int last = -1;
	private boolean full = false;

	public ConcurrentRollingArray(int capacity) {
		if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
		this.capacity = capacity;
		elements = new Object[capacity];
	}

	public int getCapacity() {
		return capacity;
	}

	public synchronized int size() {
		return full ? capacity : last + 1;
	}

	@SuppressWarnings("unchecked")
	public synchronized E get(int index) {
		int i = last == capacity-1 ? (last + 1 + index) % capacity : index;
		return (E) elements[i];
	}

	public synchronized Object[] toArray() {
		ArrayList<Object> arrayList = new ArrayList<>(size());
		if (full) {
			arrayList.addAll(Arrays.asList(elements).subList(last + 1, capacity));
			arrayList.addAll(Arrays.asList(elements).subList(0, last + 1));
		} else {
			arrayList.addAll(Arrays.asList(elements).subList(0, last + 1));
		}
		return arrayList.toArray();
	}

	@SuppressWarnings("unchecked")
	public synchronized <T> T[] toArray(T[] a) {
		Object[] data = toArray();
		if (a.length < size())
			// Make a new array of a's runtime type, but my contents:
			return (T[]) Arrays.copyOf(data, size(), a.getClass());
		System.arraycopy(data, 0, a, 0, size());
		if (a.length > size())
			a[size()] = null;
		return a;
	}

	public synchronized void add(E e) {
		last++;
		if (last >= capacity) {
			last = 0;
			full = true;
		}
		elements[last] = e;
	}
}
