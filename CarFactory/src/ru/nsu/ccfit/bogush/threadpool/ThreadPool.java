package ru.nsu.ccfit.bogush.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadPool {
	private Thread[] pool;

	private BlockingQueue<Runnable> queue;

	private boolean started = false;
	private int runningCount = 0;

	private static final String THREAD_NAME_PREFIX = "PoolThread-";

	private static final String LOGGER_NAME = "ThreadPool";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public ThreadPool(int capacity) {
		logger.trace("initialize ThreadPool of capacity " + capacity);
		if (capacity < 1) {
			logger.warn("Size is either zero or negative");
		}
		pool = new Thread[capacity];
		queue = new BlockingQueue<>();
		for (int i = 0; i < capacity; i++) {
			pool[i] = new Thread(new TaskRunner(), THREAD_NAME_PREFIX + i);
		}
	}

	public void start() {
		logger.trace("start");
		if (started) {
			logger.error("start() was already called before");
			throw new ThreadPoolException("Already started");
		}
		started = true;
		for (Thread thread: pool) {
			logger.trace("start " + thread.getName());
			thread.start();
		}
	}

	public int capacity() {
		return pool.length;
	}

	public int getRunningNumber() {
		return runningCount;
	}

	public int getAwaitingNumber() {
		return queue.size();
	}

	public void addTask(Runnable task) throws InterruptedException {
		logger.trace("add task " + task + " to the queue");
		queue.put(task);
	}

	public void subscribe(BlockingQueue.SizeSubscriber subscriber) {
		queue.subscribe(subscriber);
	}

	public class TaskRunner implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					logger.debug("request task");
					Runnable task = queue.take();
					logger.debug("task " + task + " taken");
					++runningCount;
					logger.debug("run task");
					task.run();
					--runningCount;
				}
			} catch (InterruptedException e) {
				logger.error(e);
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public static class ThreadPoolException extends RuntimeException {
		public ThreadPoolException(String message) {
			super(message);
		}
	}
}
