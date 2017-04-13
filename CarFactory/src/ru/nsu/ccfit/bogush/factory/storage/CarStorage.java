package ru.nsu.ccfit.bogush.factory.storage;

import ru.nsu.ccfit.bogush.factory.thing.Car;

public class CarStorage extends Storage<Car> {
	private final CarStorageController controller;

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
		synchronized (controller) {
			controller.notifyAll();
		}
		return car;
	}
}
