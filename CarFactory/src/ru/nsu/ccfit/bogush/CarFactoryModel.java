package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.factory.CarFactory;
import ru.nsu.ccfit.bogush.factory.Supplier;
import ru.nsu.ccfit.bogush.factory.CarStorage;
import ru.nsu.ccfit.bogush.factory.CarStorageController;
import ru.nsu.ccfit.bogush.factory.Storage;
import ru.nsu.ccfit.bogush.factory.CarStore;
import ru.nsu.ccfit.bogush.factory.Car;

public class CarFactoryModel {
	private CarFactory carFactory;

	private Storage<Car.Body> bodyStorage;
	private Storage<Car.Engine> engineStorage;
	private Storage<Car.Accessories> accessoriesStorage;

	private CarStorageController carStorageController;
	private CarStorage carStorage;

	private CarStore store;

	private Supplier[] accessorySuppliers;
	private Supplier<Car.Engine> engineSupplier;
	private Supplier<Car.Body> bodySupplier;

	private int carDealersCount;
	private int workersCount;
	private int accessorySuppliersCount;

	private boolean loggingSales;

	private static final String LOGGER_NAME = "Model";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public CarFactoryModel(Config config) {
		logger.trace("initialize");
		carDealersCount = config.getCarDealers();
		workersCount = config.getWorkers();
		accessorySuppliersCount = config.getAccessorySuppliers();

		carFactory = new CarFactory(config.getWorkers());

		bodyStorage = new Storage<>(Car.Body.class, config.getCarBodyStorageSize());
		engineStorage = new Storage<>(Car.Engine.class, config.getEngineStorageSize());
		accessoriesStorage = new Storage<>(Car.Accessories.class, config.getAccessoryStorageSize());

		carStorageController = new CarStorageController(carFactory);
		carStorage = new CarStorage(carStorageController, config.getCarStorageSize());
		carStorageController.setCarStorage(carStorage);

		accessorySuppliers = new Supplier[config.getAccessorySuppliers()];
		for (int i = 0; i < accessorySuppliers.length; i++) {
			accessorySuppliers[i] = new Supplier<>(accessoriesStorage, Car.Accessories.class);
		}
		engineSupplier = new Supplier<>(engineStorage, Car.Engine.class);
		bodySupplier = new Supplier<>(bodyStorage, Car.Body.class);

		store = new CarStore(carStorage, config.getCarDealers());

		carFactory.setCarBodyStorage(bodyStorage);
		carFactory.setCarEngineStorage(engineStorage);
		carFactory.setCarAccessoriesStorage(accessoriesStorage);
		carFactory.setCarStorage(carStorage);

		loggingSales = config.isLoggingSales();
		store.setLoggingSales(loggingSales);
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

	public boolean isLoggingSales() {
		return loggingSales;
	}

	public void setLoggingSales(boolean loggingSales) {
		logger.trace("set logging sales to " + loggingSales);
		this.loggingSales = loggingSales;
		store.setLoggingSales(loggingSales);
	}

	public void toggleLoggingSales() {
		logger.trace("toggle logging sales");
		setLoggingSales(!loggingSales);
	}
}
