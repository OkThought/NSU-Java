package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AssembleTask extends SimplyNamed implements Runnable {
	private Storage<Car.Accessories>    carAccessoryStorage;
	private Storage<Car.Body>           carBodyStorage;
	private Storage<Car.Engine>         carEngineStorage;
	private Storage<Car>                carStorage;

	private static final String LOGGER_NAME = "AssembleTask";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public AssembleTask() {
		logger.trace("initialize");
	}

	public void setCarAccessoryStorage(Storage<Car.Accessories> carAccessoryStorage) {
		this.carAccessoryStorage = carAccessoryStorage;
	}

	public void setCarBodyStorage(Storage<Car.Body> carBodyStorage) {
		this.carBodyStorage = carBodyStorage;
	}

	public void setCarEngineStorage(Storage<Car.Engine> carEngineStorage) {
		this.carEngineStorage = carEngineStorage;
	}

	public void setCarStorage(Storage<Car> carStorage) {
		this.carStorage = carStorage;
	}

	@Override
	public void run() {
		try {
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
		}
	}
}
