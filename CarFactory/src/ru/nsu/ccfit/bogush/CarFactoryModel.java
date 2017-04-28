package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.factory.CarFactory;
import ru.nsu.ccfit.bogush.factory.Supplier;
import ru.nsu.ccfit.bogush.factory.storage.CarStorage;
import ru.nsu.ccfit.bogush.factory.storage.CarStorageController;
import ru.nsu.ccfit.bogush.factory.storage.Storage;
import ru.nsu.ccfit.bogush.factory.store.CarDealer;
import ru.nsu.ccfit.bogush.factory.store.CarStore;
import ru.nsu.ccfit.bogush.factory.thing.Car;

public class CarFactoryModel {
	private CarFactory carFactory;

	private Storage<Car.Body> bodyStorage;
	private Storage<Car.Engine> engineStorage;
	private Storage<Car.Accessories> accessoriesStorage;

	private CarStorageController carStorageController;
	private CarStorage carStorage;

	private CarDealer[] dealers;
	private CarStore store;

	private Supplier[] accessorySuppliers;
	private Supplier<Car.Engine> engineSupplier;
	private Supplier<Car.Body> bodySupplier;

	private int carDealersCount;
	private int workersCount;
	private int accessorySuppliersCount;

	private Logger logger = LogManager.getRootLogger();

	public CarFactoryModel(Config config) {
		carDealersCount = config.carDealers;
		workersCount = config.workers;
		accessorySuppliersCount = config.accessorySuppliers;

		carFactory = new CarFactory(config.workers);
		bodyStorage = new Storage<>(Car.Body.class, config.carBodyStorageSize);
		engineStorage = new Storage<>(Car.Engine.class, config.engineStorageSize);
		accessoriesStorage = new Storage<>(Car.Accessories.class, config.accessoryStorageSize);
		accessorySuppliers = new Supplier[config.accessorySuppliers];
		for (int i = 0; i < accessorySuppliers.length; i++) {
			accessorySuppliers[i] = new Supplier<>(accessoriesStorage, Car.Accessories.class);
		}
		carStorageController = new CarStorageController(carFactory);
		carStorage = new CarStorage(carStorageController, config.carStorageSize);
		carStorageController.setCarStorage(carStorage);
		store = new CarStore(carStorage, config.carDealers);
		dealers = store.getDealers();
		engineSupplier = new Supplier<>(engineStorage, Car.Engine.class);
		bodySupplier = new Supplier<>(bodyStorage, Car.Body.class);

		carFactory.setCarBodyStorage(bodyStorage);
		carFactory.setCarEngineStorage(engineStorage);
		carFactory.setCarAccessoriesStorage(accessoriesStorage);
		carFactory.setCarStorage(carStorage);
	}

	public CarFactory getCarFactory() {
		return carFactory;
	}

	public Storage<Car.Body> getBodyStorage() {
		return bodyStorage;
	}

	public Storage<Car.Engine> getEngineStorage() {
		return engineStorage;
	}

	public Storage<Car.Accessories> getAccessoriesStorage() {
		return accessoriesStorage;
	}

	public CarStorageController getCarStorageController() {
		return carStorageController;
	}

	public CarStorage getCarStorage() {
		return carStorage;
	}

	public CarDealer[] getDealers() {
		return dealers;
	}

	public CarStore getStore() {
		return store;
	}

	public Supplier[] getAccessorySuppliers() {
		return accessorySuppliers;
	}

	public Supplier<Car.Engine> getEngineSupplier() {
		return engineSupplier;
	}

	public Supplier<Car.Body> getBodySupplier() {
		return bodySupplier;
	}

	public int getCarDealersCount() {
		return carDealersCount;
	}

	public int getWorkersCount() {
		return workersCount;
	}

	public int getAccessorySuppliersCount() {
		return accessorySuppliersCount;
	}

	public Logger getLogger() {
		return logger;
	}
}
