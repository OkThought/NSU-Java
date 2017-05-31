package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class AssembleTask extends SimplyNamed implements Runnable {
	private Storage<Car.Accessories>    carAccessoryStorage;
	private Storage<Car.Body>           carBodyStorage;
	private Storage<Car.Engine>         carEngineStorage;
	private Storage<Car>                carStorage;

	private static final String LOGGER_NAME = "AssembleTask";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public AssembleTask() {
		logger.traceEntry();
		logger.traceExit();
	}

	public void setCarAccessoryStorage(Storage<Car.Accessories> carAccessoryStorage) {
		logger.traceEntry();
		this.carAccessoryStorage = carAccessoryStorage;
		logger.traceExit();
	}

	public void setCarBodyStorage(Storage<Car.Body> carBodyStorage) {
		logger.traceEntry();
		this.carBodyStorage = carBodyStorage;
		logger.traceExit();
	}

	public void setCarEngineStorage(Storage<Car.Engine> carEngineStorage) {
		logger.traceEntry();
		this.carEngineStorage = carEngineStorage;
		logger.traceExit();
	}

	public void setCarStorage(Storage<Car> carStorage) {
		logger.traceEntry();
		this.carStorage = carStorage;
		logger.traceExit();
	}

	@Override
	public void run() {
		try {
			logger.traceEntry();
			logger.trace("start assembling car");
			Car.Engine engine = carEngineStorage.take();
			logger.trace(engine + " taken");
			Car.Body body = carBodyStorage.take();
			logger.trace(body + " taken");
			Car.Accessories accessories = carAccessoryStorage.take();
			logger.trace(accessories + " taken");
			Car car = new Car(engine, body, accessories);
			logger.trace(car + " assembled");
			carStorage.store(car);
		} catch (InterruptedException e) {
			logger.trace("interrupted");
			Thread.currentThread().interrupt();
		} finally {
			logger.traceExit();
		}
	}
}
