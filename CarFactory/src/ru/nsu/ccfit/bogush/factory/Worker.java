package ru.nsu.ccfit.bogush.factory;

import ru.nsu.ccfit.bogush.factory.storage.Storage;
import ru.nsu.ccfit.bogush.factory.thing.Car;

public class Worker extends SimplyNamed implements Runnable {
	private Storage<Car.Accessories> carAccessoryStorage;
	private Storage<Car.Body>        carBodyStorage;
	private Storage<Car.Engine>      carEngineStorage;
	private Storage<Car>            carStorage;

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
			Car.Engine engine = carEngineStorage.take();
			System.out.println(this + ": " + engine + " taken");
			Car.Body body = carBodyStorage.take();
			System.out.println(this + ": " + body + " taken");
			Car.Accessories accessories = carAccessoryStorage.take();
			System.out.println(this + ": " + accessories + " taken");
			Car car = new Car(engine, body, accessories);
			System.out.println(this + ": " + car + " assembled");
			carStorage.store(car);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
