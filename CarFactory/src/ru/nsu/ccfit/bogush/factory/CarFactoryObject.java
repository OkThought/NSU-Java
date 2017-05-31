package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarFactoryObject extends SimplyNamed implements Identifiable {
	private static final String LOGGER_NAME = "CarFactoryObject";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	private final long id;

	CarFactoryObject() {
		logger.traceEntry();
		this.id = Thread.currentThread().getId() * 13 + this.hashCode();
		logger.traceExit();
	}

	@Override
	public long getId() {
		logger.traceEntry();
		return logger.traceExit(id);
	}

	@Override
	public String toString() {
		return super.toString() + "-" + String.valueOf(getId());
	}
}
