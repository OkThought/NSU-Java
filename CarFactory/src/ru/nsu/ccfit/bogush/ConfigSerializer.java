package ru.nsu.ccfit.bogush;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigSerializer {
	private static final String ENGINE_STORAGE_SIZE_PROP = "EngineStorageSize";
	private static final String CAR_BODY_STORAGE_SIZE_PROP = "CarBodyStorageSize";
	private static final String ACCESSORY_STORAGE_SIZE_PROP = "AccessoryStorageSize";
	private static final String CAR_STORAGE_SIZE_PROP = "CarStorageSize";
	private static final String ACCESSORY_SUPPLIERS_PROP = "AccessorySuppliers";
	private static final String WORKERS_PROP = "Workers";
	private static final String CAR_DEALERS_PROP = "CarDealers";
	private static final String LOG_SALE_PROP = "LogSale";
	private static final String CLASS_PATH =
			ConfigSerializer.class.getResource(ConfigSerializer.class.getSimpleName() + ".class").getPath();
	private static final String CLASS_DIR_PATH =
			CLASS_PATH.substring(0, CLASS_PATH.lastIndexOf('/'));
	private static final String DEFAULT_PROPERTIES_FILE_PATH = CLASS_DIR_PATH + "/default.properties";
	private final Properties defaultProperties = new Properties();
	{
		try (FileInputStream inputStream = new FileInputStream(DEFAULT_PROPERTIES_FILE_PATH)) {
			defaultProperties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Properties properties = new Properties(defaultProperties);

	public ConfigSerializer() {}

	public ConfigSerializer(Config config) {
		properties.setProperty(ENGINE_STORAGE_SIZE_PROP, String.valueOf(config.engineStorageSize));
		properties.setProperty(CAR_BODY_STORAGE_SIZE_PROP, String.valueOf(config.carBodyStorageSize));
		properties.setProperty(ACCESSORY_STORAGE_SIZE_PROP, String.valueOf(config.accessoryStorageSize));
		properties.setProperty(CAR_STORAGE_SIZE_PROP, String.valueOf(config.carStorageSize));
		properties.setProperty(ACCESSORY_SUPPLIERS_PROP, String.valueOf(config.accessorySuppliers));
		properties.setProperty(WORKERS_PROP, String.valueOf(config.workers));
		properties.setProperty(CAR_DEALERS_PROP, String.valueOf(config.carDealers));
		properties.setProperty(LOG_SALE_PROP, String.valueOf(config.logSale));
	}

	public Config load(String configFilePath) throws IOException {
		try (FileInputStream inputStream = new FileInputStream(configFilePath)) {
			properties.load(inputStream);
		} catch (IOException e) {
			throw e;
		}
		Config config = new Config();
		config.engineStorageSize = Integer.parseInt(properties.getProperty(ENGINE_STORAGE_SIZE_PROP));
		config.carBodyStorageSize = Integer.parseInt(properties.getProperty(CAR_BODY_STORAGE_SIZE_PROP));
		config.accessoryStorageSize = Integer.parseInt(properties.getProperty(ACCESSORY_STORAGE_SIZE_PROP));
		config.carStorageSize = Integer.parseInt(properties.getProperty(CAR_STORAGE_SIZE_PROP));
		config.accessorySuppliers = Integer.parseInt(properties.getProperty(ACCESSORY_SUPPLIERS_PROP));
		config.workers = Integer.parseInt(properties.getProperty(WORKERS_PROP));
		config.carDealers = Integer.parseInt(properties.getProperty(CAR_DEALERS_PROP));
		config.logSale = Boolean.parseBoolean(properties.getProperty(LOG_SALE_PROP));
		return config;
	}

	public void store(String configFilePath) throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream(configFilePath)) {
			properties.store(outputStream, null);
		} catch (IOException e) {
			throw e;
		}
	}
}
