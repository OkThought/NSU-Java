package ru.nsu.ccfit.bogush.factory.storage;

import ru.nsu.ccfit.bogush.factory.CarFactory;
import ru.nsu.ccfit.bogush.factory.SimplyNamed;

public class CarStorageController extends SimplyNamed implements Runnable {
	private CarStorage carStorage;
	private CarFactory carFactory;

	public CarStorageController(CarFactory carFactory) {
		this.carFactory = carFactory;
	}

	public void setCarStorage(CarStorage carStorage) {
		this.carStorage = carStorage;
	}

	private int carsNeeded() {
//		return carStorage.isFull() ? 0 : 1;
		int carsBeingMade = carFactory.getThreadPool().getRunningNumber();
		int waitingWorkers = carFactory.getThreadPool().getAwaitingNumber();
		return carStorage.capacity() - carStorage.size() - carsBeingMade - waitingWorkers;
	}

	@Override
	public void run() {
		try {
			while (true) {
				synchronized (this) {
					carFactory.requestCars(carsNeeded());
					wait();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
