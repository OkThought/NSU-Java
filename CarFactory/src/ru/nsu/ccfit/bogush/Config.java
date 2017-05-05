package ru.nsu.ccfit.bogush;

public class Config {
	private int engineStorageSize;
	private int carBodyStorageSize;
	private int accessoryStorageSize;
	private int carStorageSize;
	private int accessorySuppliers;
	private int workers;
	private int carDealers;

	private boolean loggingSales;

	public int getEngineStorageSize() {
		return engineStorageSize;
	}

	public void setEngineStorageSize(int engineStorageSize) {
		this.engineStorageSize = engineStorageSize;
	}

	public int getCarBodyStorageSize() {
		return carBodyStorageSize;
	}

	public void setCarBodyStorageSize(int carBodyStorageSize) {
		this.carBodyStorageSize = carBodyStorageSize;
	}

	public int getAccessoryStorageSize() {
		return accessoryStorageSize;
	}

	public void setAccessoryStorageSize(int accessoryStorageSize) {
		this.accessoryStorageSize = accessoryStorageSize;
	}

	public int getCarStorageSize() {
		return carStorageSize;
	}

	public void setCarStorageSize(int carStorageSize) {
		this.carStorageSize = carStorageSize;
	}

	public int getAccessorySuppliers() {
		return accessorySuppliers;
	}

	public void setAccessorySuppliers(int accessorySuppliers) {
		this.accessorySuppliers = accessorySuppliers;
	}

	public int getWorkers() {
		return workers;
	}

	public void setWorkers(int workers) {
		this.workers = workers;
	}

	public int getCarDealers() {
		return carDealers;
	}

	public void setCarDealers(int carDealers) {
		this.carDealers = carDealers;
	}

	public boolean isLoggingSales() {
		return loggingSales;
	}

	public void setLoggingSales(boolean loggingSales) {
		this.loggingSales = loggingSales;
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
