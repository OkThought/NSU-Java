package ru.nsu.ccfit.bogush;

public class LoggingConfiguration {
	public static final String DEFAULT_LOGGER_CONFIG_FILE = "log.xml";

	public static void setConfigFile() {
		setConfigFile(DEFAULT_LOGGER_CONFIG_FILE);
	}

	public static void setConfigFile(String filePath) {
		System.setProperty("log4j.configurationFile", filePath);
	}
}
