package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	private int engineStorageSize;
	private int carBodyStorageSize;
	private int accessoryStorageSize;
	private int carStorageSize;
	private int accessorySuppliers;
	private int workers;
	private int carDealers;

	private boolean loggingSales;

	private static final Logger logger = LogManager.getLogger();

	public int getEngineStorageSize() {
		logger.traceEntry();
		return logger.traceExit(engineStorageSize);
	}

	public void setEngineStorageSize(int engineStorageSize) {
		logger.traceEntry();
		this.engineStorageSize = engineStorageSize;
		logger.traceExit();
	}

	public int getCarBodyStorageSize() {
		logger.traceEntry();
		return logger.traceExit(carBodyStorageSize);
	}

	public void setCarBodyStorageSize(int carBodyStorageSize) {
		logger.traceEntry();
		this.carBodyStorageSize = carBodyStorageSize;
		logger.traceExit();
	}

	public int getAccessoryStorageSize() {
		logger.traceEntry();
		return logger.traceExit(accessoryStorageSize);
	}

	public void setAccessoryStorageSize(int accessoryStorageSize) {
		logger.traceEntry();
		this.accessoryStorageSize = accessoryStorageSize;
		logger.traceExit();
	}

	public int getCarStorageSize() {
		logger.traceEntry();
		return logger.traceExit(carStorageSize);
	}

	public void setCarStorageSize(int carStorageSize) {
		logger.traceEntry();
		this.carStorageSize = carStorageSize;
		logger.traceExit();
	}

	public int getAccessorySuppliers() {
		logger.traceEntry();
		return logger.traceExit(accessorySuppliers);
	}

	public void setAccessorySuppliers(int accessorySuppliers) {
		logger.traceEntry();
		this.accessorySuppliers = accessorySuppliers;
		logger.traceExit();
	}

	public int getWorkers() {
		logger.traceEntry();
		return logger.traceExit(workers);
	}

	public void setWorkers(int workers) {
		logger.traceEntry();
		this.workers = workers;
		logger.traceExit();
	}

	public int getCarDealers() {
		logger.traceEntry();
		return logger.traceExit(carDealers);
	}

	public void setCarDealers(int carDealers) {
		logger.traceEntry();
		this.carDealers = carDealers;
		logger.traceExit();
	}

	public boolean isLoggingSales() {
		logger.traceEntry();
		return logger.traceExit(loggingSales);
	}

	public void setLoggingSales(boolean loggingSales) {
		logger.traceEntry();
		this.loggingSales = loggingSales;
		logger.traceExit();
	}

	@Override
	public String toString() {
		return "Config{" +
				"\n\tengineStorageSize=" + engineStorageSize +
				"\n\tcarBodyStorageSize=" + carBodyStorageSize +
				"\n\taccessoryStorageSize=" + accessoryStorageSize +
				"\n\tcarStorageSize=" + carStorageSize +
				"\n\taccessorySuppliers=" + accessorySuppliers +
				"\n\tworkers=" + workers +
				"\n\tcarDealers=" + carDealers +
				"\n\tloggingSales=" + loggingSales +
				"\n}";
	}
}
