package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Car extends CarFactoryObject {
	private final Engine engine;
	private final Body body;
	private final Accessories accessories;

	private static final String LOGGER_NAME = "Car";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public Car(Engine engine, Body body, Accessories accessories) {
		logger.traceEntry();
		this.engine = engine;
		this.body = body;
		this.accessories = accessories;
		logger.traceExit();
	}

	public String getInfo() {
		logger.traceEntry();
		return logger.traceExit(super.toString() + '(' + engine + ", " + body + ", " + accessories + ')');
	}

	public static class Accessories extends CarFactoryObject {}

	public static class Body extends CarFactoryObject {}

	public static class Engine extends CarFactoryObject {}
}
