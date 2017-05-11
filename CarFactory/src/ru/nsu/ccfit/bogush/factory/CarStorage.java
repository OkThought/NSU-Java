package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarStorage extends Storage<Car> {
	private final CarStorageController controller;

	private static final String LOGGER_NAME = "CarStorage";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public CarStorage(CarStorageController controller, int maxSize) {
		super(Car.class, maxSize);
		this.controller = controller;
	}

	public CarStorageController getController() {
		return controller;
	}

	@Override
	public Car take() throws InterruptedException {
		Car car = super.take();
		logger.trace(car + " taken from " + this);
		controller.carTaken();
		return car;
	}
}
