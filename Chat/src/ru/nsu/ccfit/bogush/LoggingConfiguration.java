package ru.nsu.ccfit.bogush;

public class LoggingConfiguration {
	public static final String DEFAULT_LOGGER_CONFIG_FILE = "log.xml";

	public static void addConfigFile(String filePath) {
		System.setProperty("log4j.configurationFile", filePath);
	}
}
