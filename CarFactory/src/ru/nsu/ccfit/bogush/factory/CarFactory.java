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
		logger.traceEntry();
		logger.trace("initialize CarFactory with " + amountOfWorkers + " workers");
		if (amountOfWorkers < 1) {
			logger.fatal("amount of workers is either zero or negative");
			throw new IllegalArgumentException("amount of workers must be positive");
		}
		this.workers = new ThreadPool(amountOfWorkers);
		logger.traceExit();
	}

	public void setCarAccessoriesStorage(Storage<Car.Accessories> carAccessoriesStorage) {
		logger.traceEntry();
		this.carAccessoriesStorage = carAccessoriesStorage;
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

	public void setCarStorage(CarStorage carStorage) {
		logger.traceEntry();
		this.carStorage = carStorage;
		logger.traceExit();
	}

	public Storage<Car.Accessories> getCarAccessoriesStorage() {
		logger.traceEntry();
		return logger.traceExit(carAccessoriesStorage);
	}

	public Storage<Car.Body> getCarBodyStorage() {
		logger.traceEntry();
		return logger.traceExit(carBodyStorage);
	}

	public Storage<Car.Engine> getCarEngineStorage() {
		logger.traceEntry();
		return logger.traceExit(carEngineStorage);
	}

	public CarStorage getCarStorage() {
		logger.traceEntry();
		return logger.traceExit(carStorage);
	}

	public ThreadPool getThreadPool() {
		logger.traceEntry();
		return logger.traceExit(workers);
	}

	public void requestCars(int count) throws InterruptedException {
		logger.traceEntry();
		logger.trace( count + " cars requested");
		for (int i = 0; i < count; i++) {
			workers.addTask(createTask());
		}
		logger.traceExit();
	}

	private AssembleTask createTask() {
		logger.traceEntry();
		AssembleTask assembleTask = new AssembleTask();
		assembleTask.setCarEngineStorage(carEngineStorage);
		assembleTask.setCarBodyStorage(carBodyStorage);
		assembleTask.setCarAccessoryStorage(carAccessoriesStorage);
		assembleTask.setCarStorage(carStorage);
		return logger.traceExit(assembleTask);
	}
}
