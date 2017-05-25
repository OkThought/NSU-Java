package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarStorageController extends SimplyNamed implements Runnable {
	private CarStorage carStorage;
	private CarFactory carFactory;
	private Thread thread;
	private boolean updateRequested = false;

	private static final Object lock = new Object();

	private static final String LOGGER_NAME = "CarStorageController";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public CarStorageController(CarFactory carFactory) {
		logger.trace("initialize");
		this.carFactory = carFactory;
		thread = new Thread(this, toString());
	}

	public void setCarStorage(CarStorage carStorage) {
		logger.trace("set car storage to " + carStorage);
		this.carStorage = carStorage;
	}

	public void update() {
		logger.trace("update");
		synchronized (lock) {
			updateRequested = true;
			lock.notifyAll();
		}
	}

	private int carsNeeded() {
		logger.trace("calculate amount of cars needed");
		int taskQueueSize = carFactory.getThreadPool().getAwaitingNumber();
		int carStorageSize = carStorage.size();
		int carStorageCapacity = carStorage.capacity();
		int result = carStorageCapacity - carStorageSize - taskQueueSize;
		logger.trace("taskQueueSize = " + taskQueueSize);
		logger.trace("carStorageSize = " + carStorageSize);
		logger.trace("carStorageCapacity = " + carStorageCapacity);
		logger.trace("carsNeeded = " + result);
		return result;
	}

	public void start() {
		thread.start();
	}

	public void stop() {
		thread.interrupt();
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				carFactory.requestCars(carsNeeded());
				synchronized (lock) {
					while (!updateRequested) {
						lock.wait();
					}
					updateRequested = false;
				}
			}
		} catch (InterruptedException e) {
			logger.trace("interrupted");
		} finally {
			logger.trace("stopped");
		}
	}
}
