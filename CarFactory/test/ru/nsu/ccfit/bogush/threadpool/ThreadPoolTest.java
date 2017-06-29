package ru.nsu.ccfit.bogush.threadpool;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

class ThreadPoolTest {
	private ThreadPool threadPool = new ThreadPool(10);
	private int cnt = 0;
	private final Object lock = new Object();

	private void generateTasks(int numberOfTasks) throws InterruptedException {
		for (int i = 0; i < numberOfTasks; i++) {
			threadPool.addTask(new TestTask());
		}
	}

	@Test
	void test1000Tasks() throws InterruptedException {
		cnt = 0;
		generateTasks(1000);
		threadPool.start();
		Thread.sleep(100);
		assertEquals(1000, cnt);
	}

	@Test
	void test10000Tasks() throws InterruptedException {
		cnt = 0;
		generateTasks(10000);
		threadPool.start();
		Thread.sleep(100);
		assertEquals(10000, cnt);
	}

	private class TestTask implements Runnable {
		@Override
		public void run() {
			synchronized (lock) {
				cnt++;
			}
		}
	}
}