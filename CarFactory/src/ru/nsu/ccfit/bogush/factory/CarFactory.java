package ru.nsu.ccfit.bogush.factory;

import ru.nsu.ccfit.bogush.factory.storage.CarStorage;
import ru.nsu.ccfit.bogush.factory.storage.Storage;
import ru.nsu.ccfit.bogush.factory.thing.CarAccessory;
import ru.nsu.ccfit.bogush.factory.thing.CarBody;
import ru.nsu.ccfit.bogush.factory.thing.CarEngine;
import ru.nsu.ccfit.bogush.threadpool.ThreadPool;

public class CarFactory extends SimplyNamed {
	private Storage<CarAccessory> carAccessoryStorage;
	private Storage<CarBody> carBodyStorage;
	private Storage<CarEngine> carEngineStorage;
	private CarStorage carStorage;
	private ThreadPool threadPool;

	public CarFactory(int amountOfWorkers) {
		this.threadPool = new ThreadPool(amountOfWorkers);
	}

	public void setCarAccessoryStorage(Storage<CarAccessory> carAccessoryStorage) {
		this.carAccessoryStorage = carAccessoryStorage;
	}

	public void setCarBodyStorage(Storage<CarBody> carBodyStorage) {
		this.carBodyStorage = carBodyStorage;
	}

	public void setCarEngineStorage(Storage<CarEngine> carEngineStorage) {
		this.carEngineStorage = carEngineStorage;
	}

	public void setCarStorage(CarStorage carStorage) {
		this.carStorage = carStorage;
	}

	public Storage<CarAccessory> getCarAccessoryStorage() {
		return carAccessoryStorage;
	}

	public Storage<CarBody> getCarBodyStorage() {
		return carBodyStorage;
	}

	public Storage<CarEngine> getCarEngineStorage() {
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
		worker.setCarAccessoryStorage(carAccessoryStorage);
		worker.setCarStorage(carStorage);
		return worker;
	}

	public ThreadPool getThreadPool() {
		return threadPool;
	}
}
