package ru.nsu.ccfit.bogush.factory;

import ru.nsu.ccfit.bogush.factory.storage.Storage;
import ru.nsu.ccfit.bogush.factory.thing.Car;
import ru.nsu.ccfit.bogush.factory.thing.CarAccessory;
import ru.nsu.ccfit.bogush.factory.thing.CarBody;
import ru.nsu.ccfit.bogush.factory.thing.CarEngine;

public class Worker extends SimplyNamed implements Runnable {
	private Storage<CarAccessory> carAccessoryStorage;
	private Storage<CarBody>        carBodyStorage;
	private Storage<CarEngine>      carEngineStorage;
	private Storage<Car>            carStorage;

	public void setCarAccessoryStorage(Storage<CarAccessory> carAccessoryStorage) {
		this.carAccessoryStorage = carAccessoryStorage;
	}

	public void setCarBodyStorage(Storage<CarBody> carBodyStorage) {
		this.carBodyStorage = carBodyStorage;
	}

	public void setCarEngineStorage(Storage<CarEngine> carEngineStorage) {
		this.carEngineStorage = carEngineStorage;
	}

	public void setCarStorage(Storage<Car> carStorage) {
		this.carStorage = carStorage;
	}

	@Override
	public void run() {
		try {
			CarEngine engine = carEngineStorage.take();
			System.out.println(this + ": " + engine + " taken");
			CarBody body = carBodyStorage.take();
			System.out.println(this + ": " + body + " taken");
			CarAccessory[] accessories = new CarAccessory[Car.ACCESSORY_NUMBER];
			for (int i = 0; i < Car.ACCESSORY_NUMBER; i++) {
				accessories[i] = carAccessoryStorage.take();
				System.out.println(this + ": " + accessories[i] + " taken");
			}
			Car car = new Car(engine, body, accessories);
			System.out.println(this + ": " + car + " assembled");
			carStorage.store(car);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
