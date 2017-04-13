package ru.nsu.ccfit.bogush.factory.store;

import ru.nsu.ccfit.bogush.factory.storage.CarStorage;
import ru.nsu.ccfit.bogush.factory.thing.Car;
import ru.nsu.ccfit.bogush.factory.thing.periodical.SimplePeriodical;

public class CarDealer extends SimplePeriodical implements Runnable {
	private static final long defaultPeriod = 1000; // 1 sec
	private CarStorage storage;

	private CarStore store;
	private final Thread thread;

	public CarDealer(CarStorage storage, CarStore store) {
		super(defaultPeriod);
		this.storage = storage;
		this.store = store;
		this.thread = new Thread(this);
	}

	public CarDealer(CarStorage storage, CarStore store, long period) {
		super(period);
		this.storage = storage;
		this.store = store;
		this.thread = new Thread(this);
	}

	@Override
	public void run() {
		while(true) {
			try {
				Car car = storage.take();
				System.out.println(this + ": took " + car);
				store.sell(car);
				System.out.println(this + ": sold " + car);
				Thread.sleep(getPeriod());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public Thread getThread() {
		return thread;
	}
}
