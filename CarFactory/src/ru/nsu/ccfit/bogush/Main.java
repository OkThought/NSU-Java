package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.factory.*;
import ru.nsu.ccfit.bogush.factory.storage.CarStorage;
import ru.nsu.ccfit.bogush.factory.storage.CarStorageController;
import ru.nsu.ccfit.bogush.factory.storage.Storage;
import ru.nsu.ccfit.bogush.factory.store.CarStore;
import ru.nsu.ccfit.bogush.factory.thing.CarAccessory;
import ru.nsu.ccfit.bogush.factory.thing.CarBody;
import ru.nsu.ccfit.bogush.factory.thing.CarEngine;
import ru.nsu.ccfit.bogush.view.FactoryView;

import java.io.IOException;

public class Main {
	private static final String CONFIG_FILE = "config.properties";

	private static Storage<CarBody> bodyStorage;
	private static Storage<CarEngine> engineStorage;

	private static Storage<CarAccessory> accessoryStorage;
	private static Supplier[] accessorySuppliers;

	private static CarStorageController carStorageController;
	private static CarStorage carStorage;

	private static CarStore store;

	private static CarFactory carFactory;

	private static Supplier<CarEngine> engineSupplier;
	private static Supplier<CarBody> bodySupplier;



	private static void prepare(String configFilePath) throws IOException, InterruptedException {
		Config config = new ConfigSerializer().load(configFilePath);
		carFactory = new CarFactory(config.workers);
		bodyStorage = new Storage<>(CarBody.class, config.carBodyStorageSize);
		engineStorage = new Storage<>(CarEngine.class, config.engineStorageSize);
		accessoryStorage = new Storage<>(CarAccessory.class, config.accessoryStorageSize);
		accessorySuppliers = new Supplier[config.accessorySuppliers];
		for (int i = 0; i < accessorySuppliers.length; i++) {
			accessorySuppliers[i] = new Supplier<>(accessoryStorage, CarAccessory.class);
		}
		carStorageController = new CarStorageController(carFactory);
		carStorage = new CarStorage(carStorageController, config.carStorageSize);
		carStorageController.setCarStorage(carStorage);
		store = new CarStore(carStorage, config.carDealers);
		engineSupplier = new Supplier<>(engineStorage, CarEngine.class);
		bodySupplier = new Supplier<>(bodyStorage, CarBody.class);

		carFactory.setCarBodyStorage(bodyStorage);
		carFactory.setCarEngineStorage(engineStorage);
		carFactory.setCarAccessoryStorage(accessoryStorage);
		carFactory.setCarStorage(carStorage);
	}

	public static void main(String[] args) {
		try {
			prepare(CONFIG_FILE);
			@SuppressWarnings("unchecked")
			FactoryView view = new FactoryView(carFactory, store, engineSupplier, bodySupplier, accessorySuppliers);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
