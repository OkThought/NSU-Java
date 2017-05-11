package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarStorageController extends SimplyNamed implements Runnable {
	private CarStorage carStorage;
	private CarFactory carFactory;
	private Thread thread;
	private boolean updateRequested = false;

	private static final Object sync = new Object();

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

	public void carTaken() {
		logger.trace("car taken from storage - update");
		updateRequested = true;
		synchronized (sync) {
			sync.notifyAll();
		}
	}

	private int carsNeeded() {
		logger.debug("calculate amount of cars needed");
		int carsAssembling = carFactory.getThreadPool().getRunningNumber();
		logger.debug("carsAssembling = " + carsAssembling);
		int taskQueueSize = carFactory.getThreadPool().getAwaitingNumber();
		logger.debug("taskQueueSize = " + taskQueueSize);
		int carStorageSize = carStorage.size();
		logger.debug("carStorageSize = " + carStorageSize);
		int carStorageCapacity = carStorage.capacity();
		logger.debug("carStorageCapacity = " + carStorageCapacity);
		int result = carStorageCapacity - carStorageSize - carsAssembling - taskQueueSize;
		logger.debug("carsNeeded = " + result);
		return result;
	}

	public void start() {
		thread.start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				carFactory.requestCars(carsNeeded());
				synchronized (sync) {
					while (!updateRequested) {
						sync.wait();
					}
				}
				updateRequested = false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
