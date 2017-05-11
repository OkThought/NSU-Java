package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.threadpool.ThreadPool;

public class CarFactory extends SimplyNamed {
	private Storage<Car.Accessories> carAccessoriesStorage;
	private Storage<Car.Body> carBodyStorage;
	private Storage<Car.Engine> carEngineStorage;
	private CarStorage carStorage;
	private ThreadPool workers;

	private static final String LOGGER_NAME = "CarFactory";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public CarFactory(int amountOfWorkers) {
		logger.trace("initialize CarFactory with " + amountOfWorkers + " workers");
		if (amountOfWorkers < 1) {
			logger.fatal("amount of workers is either zero or negative");
			throw new IllegalArgumentException("amount of workers must be positive");
		}
		this.workers = new ThreadPool(amountOfWorkers);
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

	public ThreadPool getThreadPool() {
		return workers;
	}

	public void requestCars(int count) throws InterruptedException {
		logger.trace( count + " cars requested");
		for (int i = 0; i < count; i++) {
			workers.addTask(createTask());
		}
	}

	private AssembleTask createTask() {
		logger.trace("create task");
		AssembleTask assembleTask = new AssembleTask();
		assembleTask.setCarEngineStorage(carEngineStorage);
		assembleTask.setCarBodyStorage(carBodyStorage);
		assembleTask.setCarAccessoryStorage(carAccessoriesStorage);
		assembleTask.setCarStorage(carStorage);
		return assembleTask;
	}
}
