package ru.nsu.ccfit.bogush.chat.server;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConcurrentRollingArrayTest {
	@Test(expected = IllegalArgumentException.class)
	public void createEmpty() {
		new ConcurrentRollingArray<>(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNegativeCapacity() {
		new ConcurrentRollingArray<>(-1);
	}

	@Test
	public void createNonEmpty() {
		new ConcurrentRollingArray<>(100);
	}

	@Test
	public void createAndAddElements() {
		ConcurrentRollingArray<Object> array = new ConcurrentRollingArray<>(2);
		array.add(new Object());
		array.add(new Object());
		array.add(new Object());
	}

	@Test
	public void correctElementsInRightPlace() {
		int capacity = 3;
		ConcurrentRollingArray<Integer> array = new ConcurrentRollingArray<>(capacity);
		for (int i = 0; i < capacity * 3; i++) {
			array.add(i);
			int got = array.get(i % capacity);
			assertEquals(i, got);
		}
		Integer[] elements = array.toArray(new Integer[array.size()]);
		for (int i = 0; i < capacity; i++) {
			assertEquals(elements[i], array.get(i));
		}
	}
}