package ru.nsu.ccfit.bogush.threadpool;

import java.util.ArrayList;
import java.util.List;

public class ThreadPool {
	private Thread[] pool;

	private BlockingQueue<Runnable> queue;

	private List<TaskSubscriber> taskSubscribers = new ArrayList<>();
	private int running = 0;
	public ThreadPool(int size) {
		pool = new Thread[size];
		queue = new BlockingQueue<>();
		for (int i = 0; i < size; i++) {
			pool[i] = new Thread(new TaskRunner());
			pool[i].start();
		}
	}
	public int size() {
		return pool.length;
	}

	public int getRunningNumber() {
		return running;
	}

	public int getAwaitingNumber() {
		return queue.size();
	}

	public void addTask(Runnable task) throws InterruptedException {
		queue.put(task);
		queueSizeChanged();
	}

	public void subscribe(TaskSubscriber subscriber) {
		taskSubscribers.add(subscriber);
	}

	private void queueSizeChanged() {
		for (TaskSubscriber subscriber: taskSubscribers) {
			subscriber.queueSizeChanged(queue.size());
		}
	}

	public class TaskRunner implements Runnable {
		@Override
		public void run() {
			try {
				while (!Thread.interrupted()) {
					Runnable task = queue.take();
					queueSizeChanged();
					++running;
					task.run();
					--running;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
