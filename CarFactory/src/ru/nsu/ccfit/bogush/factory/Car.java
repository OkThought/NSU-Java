package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Car extends Thing {
	private final Engine engine;
	private final Body body;
	private final Accessories accessories;

	private static final String LOGGER_NAME = "Car";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public Car(Engine engine, Body body, Accessories accessories) {
		logger.trace("initialize");
		this.engine = engine;
		this.body = body;
		this.accessories = accessories;
	}

	public String getInfo() {
		return super.toString() + '(' + engine + ", " + body + ", " + accessories + ')';
	}

	public static class Accessories extends Thing {}

	public static class Body extends Thing {}

	public static class Engine extends Thing {}
}
