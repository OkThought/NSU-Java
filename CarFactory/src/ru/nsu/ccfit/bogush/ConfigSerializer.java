package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

class ConfigSerializer {
	private static final String ENGINE_STORAGE_SIZE_PROP = "EngineStorageSize";
	private static final String CAR_BODY_STORAGE_SIZE_PROP = "CarBodyStorageSize";
	private static final String ACCESSORY_STORAGE_SIZE_PROP = "AccessoryStorageSize";
	private static final String CAR_STORAGE_SIZE_PROP = "CarStorageSize";
	private static final String ACCESSORY_SUPPLIERS_PROP = "AccessorySuppliers";
	private static final String WORKERS_PROP = "Workers";
	private static final String CAR_DEALERS_PROP = "CarDealers";
	private static final String LOG_SALES_PROP = "LogSales";
	private static final String DEFAULT_PROPERTIES_FILE_PATH = "default.properties";
	private static final Properties defaultProperties = new Properties();
	static {
		try (FileInputStream inputStream = new FileInputStream(DEFAULT_PROPERTIES_FILE_PATH)) {
			defaultProperties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final String LOGGER_NAME = "ConfigSerializer";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	private Properties properties = new Properties(defaultProperties);

	public ConfigSerializer() {
		logger.traceEntry();
		logger.traceExit();
	}

	public ConfigSerializer(Config config) {
		logger.traceEntry();
		properties.setProperty(ENGINE_STORAGE_SIZE_PROP, String.valueOf(config.getEngineStorageSize()));
		properties.setProperty(CAR_BODY_STORAGE_SIZE_PROP, String.valueOf(config.getCarBodyStorageSize()));
		properties.setProperty(ACCESSORY_STORAGE_SIZE_PROP, String.valueOf(config.getAccessoryStorageSize()));
		properties.setProperty(CAR_STORAGE_SIZE_PROP, String.valueOf(config.getCarStorageSize()));
		properties.setProperty(ACCESSORY_SUPPLIERS_PROP, String.valueOf(config.getAccessorySuppliers()));
		properties.setProperty(WORKERS_PROP, String.valueOf(config.getWorkers()));
		properties.setProperty(CAR_DEALERS_PROP, String.valueOf(config.getCarDealers()));
		properties.setProperty(LOG_SALES_PROP, String.valueOf(config.isLoggingSales()));
		logger.traceExit();
	}

	public Config load(String configFilePath) throws IOException {
		logger.traceEntry();
		logger.trace("loading config file " + configFilePath);
		try (FileInputStream inputStream = new FileInputStream(configFilePath)) {
			properties.load(inputStream);
		}
		logger.trace("config file loaded successfully");
		Config config = new Config();
		config.setEngineStorageSize(Integer.parseInt(properties.getProperty(ENGINE_STORAGE_SIZE_PROP)));
		config.setCarBodyStorageSize(Integer.parseInt(properties.getProperty(CAR_BODY_STORAGE_SIZE_PROP)));
		config.setAccessoryStorageSize(Integer.parseInt(properties.getProperty(ACCESSORY_STORAGE_SIZE_PROP)));
		config.setCarStorageSize(Integer.parseInt(properties.getProperty(CAR_STORAGE_SIZE_PROP)));
		config.setAccessorySuppliers(Integer.parseInt(properties.getProperty(ACCESSORY_SUPPLIERS_PROP)));
		config.setWorkers(Integer.parseInt(properties.getProperty(WORKERS_PROP)));
		config.setCarDealers(Integer.parseInt(properties.getProperty(CAR_DEALERS_PROP)));
		config.setLoggingSales(Boolean.parseBoolean(properties.getProperty(LOG_SALES_PROP)));
		logger.trace("config object created");
		logger.trace("config = " + config);
		return logger.traceExit(config);
	}

	public void store(String configFilePath) throws IOException {
		logger.traceEntry();
		try (FileOutputStream outputStream = new FileOutputStream(configFilePath)) {
			properties.store(outputStream, null);
		} finally {
			logger.traceExit();
		}
	}
}
