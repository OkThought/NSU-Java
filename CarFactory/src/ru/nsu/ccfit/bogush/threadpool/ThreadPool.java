package ru.nsu.ccfit.bogush.threadpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadPool {
	private final Thread[] pool;

	private final BlockingQueue<Runnable> queue;

	private boolean started = false;

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

	public void stop() {
		logger.trace("stop");
		if (!started) {
			logger.trace("not running, do nothing");
		} else {
			started = false;
			for (Thread thread: pool) {
				logger.trace("stop " + thread.getName());
				thread.interrupt();
			}
		}
	}

	public int getAwaitingNumber() {
		return queue.size();
	}

	public void addTask(Runnable task) throws InterruptedException {
		logger.trace("add task " + task + " to the queue");
		queue.put(task);
	}

	public void addTaskQueueSizeSubscriber(BlockingQueue.SizeSubscriber subscriber) {
		queue.addSizeSubscriber(subscriber);
	}

	private static final String TASK_RUNNER_LOGGER_NAME = "TaskRunner";
	private static final Logger taskRunnerLogger = LogManager.getLogger(TASK_RUNNER_LOGGER_NAME);

	public class TaskRunner implements Runnable {
		@Override
		public void run() {
			try {
				while (!Thread.interrupted()) {
					taskRunnerLogger.trace("request task");
					Runnable task = queue.take();
					taskRunnerLogger.trace("task " + task + " taken");
					taskRunnerLogger.trace("run task");
					task.run();
				}
			} catch (InterruptedException e) {
				taskRunnerLogger.trace("interrupted");
			} finally {
				taskRunnerLogger.trace("stopped");
			}
		}
	}

	public static class ThreadPoolException extends RuntimeException {
		public ThreadPoolException(String message) {
			super(message);
		}
	}
}
