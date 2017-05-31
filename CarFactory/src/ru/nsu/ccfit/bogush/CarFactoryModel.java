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
		logger.traceEntry();
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
		logger.traceExit();
	}

	public CarFactory getCarFactory() {
		logger.traceEntry();
		return logger.traceExit(carFactory);
	}

	public Storage<Car.Body> getBodyStorage() {
		logger.traceEntry();
		return logger.traceExit(bodyStorage);
	}

	public Storage<Car.Engine> getEngineStorage() {
		logger.traceEntry();
		return logger.traceExit(engineStorage);
	}

	public Storage<Car.Accessories> getAccessoriesStorage() {
		logger.traceEntry();
		return logger.traceExit(accessoriesStorage);
	}

	public CarStorageController getCarStorageController() {
		logger.traceEntry();
		return logger.traceExit(carStorageController);
	}

	public CarStorage getCarStorage() {
		logger.traceEntry();
		return logger.traceExit(carStorage);
	}

	public CarStore getStore() {
		logger.traceEntry();
		return logger.traceExit(store);
	}

	public Supplier[] getAccessorySuppliers() {
		logger.traceEntry();
		return logger.traceExit(accessorySuppliers);
	}

	public Supplier<Car.Engine> getEngineSupplier() {
		logger.traceEntry();
		return logger.traceExit(engineSupplier);
	}

	public Supplier<Car.Body> getBodySupplier() {
		logger.traceEntry();
		return logger.traceExit(bodySupplier);
	}

	public int getCarDealersCount() {
		logger.traceEntry();
		return logger.traceExit(carDealersCount);
	}

	public int getWorkersCount() {
		logger.traceEntry();
		return logger.traceExit(workersCount);
	}

	public int getAccessorySuppliersCount() {
		logger.traceEntry();
		return logger.traceExit(accessorySuppliersCount);
	}

	public boolean isLoggingSales() {
		logger.traceEntry();
		return logger.traceExit(loggingSales);
	}

	private void setLoggingSales(boolean loggingSales) {
		logger.traceEntry();
		logger.trace("set logging sales to " + loggingSales);
		this.loggingSales = loggingSales;
		store.setLoggingSales(loggingSales);
		logger.traceExit();
	}

	public void toggleLoggingSales() {
		logger.traceEntry();
		setLoggingSales(!loggingSales);
		logger.traceExit();
	}
}
