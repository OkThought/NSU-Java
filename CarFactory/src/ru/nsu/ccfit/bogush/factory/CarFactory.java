package ru.nsu.ccfit.bogush.factory;

import ru.nsu.ccfit.bogush.factory.storage.CarStorage;
import ru.nsu.ccfit.bogush.factory.storage.Storage;
import ru.nsu.ccfit.bogush.factory.thing.Car;
import ru.nsu.ccfit.bogush.threadpool.ThreadPool;

public class CarFactory extends SimplyNamed {
	private Storage<Car.Accessories> carAccessoriesStorage;
	private Storage<Car.Body> carBodyStorage;
	private Storage<Car.Engine> carEngineStorage;
	private CarStorage carStorage;
	private ThreadPool threadPool;

	public CarFactory(int amountOfWorkers) {
		this.threadPool = new ThreadPool(amountOfWorkers);
	}

	public void setCarAccessoriesStorage(Storage<Car.Accessories> carAccessoriesStorage) {
		this.carAccessoriesStorage = carAccessoriesStorage;
	}

	public void setCarBodyStorage(Storage<Car.Body> carBodyStorage) {
		this.carBodyStorage = carBodyStorage;
	}

	public void setCarEngineStorage(Storage<Car.Engine> carEngineStorage) {
		this.carEngineStorage = carEngineStorage;
	}

	public void setCarStorage(CarStorage carStorage) {
		this.carStorage = carStorage;
	}

	public Storage<Car.Accessories> getCarAccessoriesStorage() {
		return carAccessoriesStorage;
	}

	public Storage<Car.Body> getCarBodyStorage() {
		return carBodyStorage;
	}

	public Storage<Car.Engine> getCarEngineStorage() {
		return carEngineStorage;
	}

	public CarStorage getCarStorage() {
		return carStorage;
	}

	public void requestCars(int count) throws InterruptedException {
		System.out.println(this + ": new order: produce " + count + " cars");
		for (int i = 0; i < count; i++) {
			threadPool.addTask(createWorker());
		}
	}

	private Worker createWorker() {
		Worker worker = new Worker();
		worker.setCarEngineStorage(carEngineStorage);
		worker.setCarBodyStorage(carBodyStorage);
		worker.setCarAccessoryStorage(carAccessoriesStorage);
		worker.setCarStorage(carStorage);
		return worker;
	}

	public ThreadPool getThreadPool() {
		return threadPool;
	}
}
