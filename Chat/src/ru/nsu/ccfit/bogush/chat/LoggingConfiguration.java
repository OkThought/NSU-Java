package ru.nsu.ccfit.bogush.chat;

public class LoggingConfiguration {
	private static final String LOG4J_CONFIG_FILE_KEY = "log4j.configurationFile";
	public static final String DEFAULT_LOGGER_CONFIG_FILE = "log.xml";

	public static void setConfigFileToDefaultIfNotSet() {
		if (System.getProperty(LOG4J_CONFIG_FILE_KEY) == null) {
			setConfigFile(DEFAULT_LOGGER_CONFIG_FILE);
		}
	}

	public static void setConfigFile(String filePath) {
		System.setProperty(LOG4J_CONFIG_FILE_KEY, filePath);
	}
}
