package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.view.FactoryView;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	private static final String CONFIG_FILE     = "config.properties";
	private static final String LOG_CONFIG_FILE = "log4j2.xml";
	private static final String LOGGER_NAME     = "Main";
	private static final Logger logger;

	static {
		System.setProperty("log4j.configurationFile", LOG_CONFIG_FILE);
		logger = LogManager.getLogger(LOGGER_NAME);
	}

	public static void main(String[] args) {
		logger.trace("Launching application");
		try {
			logger.trace("Running prepare");
			Path configPath = Paths.get(CONFIG_FILE);
			if (!Files.exists(configPath)) {
				logger.info("Couldn't find configuration file \"{}\"", CONFIG_FILE);
				logger.info("Creating it filled with defaults");
				Path defaultConfigPath = Paths.get(ConfigSerializer.DEFAULT_PROPERTIES_FILE_PATH);
				Files.copy(defaultConfigPath, configPath);
			}
			Config config = new ConfigSerializer().load(CONFIG_FILE);
			new FactoryView(new CarFactoryModel(config));
		} catch (Exception e) {
			logger.fatal(e);
			e.printStackTrace();
			System.exit(1);
		} finally {
			logger.trace("Exiting");
		}
	}
}
