package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.factory.CarFactory;
import ru.nsu.ccfit.bogush.factory.Supplier;
import ru.nsu.ccfit.bogush.factory.storage.CarStorage;
import ru.nsu.ccfit.bogush.factory.storage.CarStorageController;
import ru.nsu.ccfit.bogush.factory.storage.Storage;
import ru.nsu.ccfit.bogush.factory.store.CarDealer;
import ru.nsu.ccfit.bogush.factory.store.CarStore;
import ru.nsu.ccfit.bogush.factory.thing.Car;

public class CarFactoryModel {
	public CarFactory carFactory;

	public Storage<Car.Body> bodyStorage;
	public Storage<Car.Engine> engineStorage;
	public Storage<Car.Accessories> accessoriesStorage;

	public CarStorageController carStorageController;
	public CarStorage carStorage;

	public CarDealer[] dealers;
	public CarStore store;

	public Supplier[] accessorySuppliers;
	public Supplier<Car.Engine> engineSupplier;
	public Supplier<Car.Body> bodySupplier;

	public int carDealersCount;
	public int workersCount;
	public int accessorySuppliersCount;

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
}
