package ru.nsu.ccfit.bogush.factory;

import ru.nsu.ccfit.bogush.factory.storage.Storage;
import ru.nsu.ccfit.bogush.factory.thing.Thing;
import ru.nsu.ccfit.bogush.factory.thing.periodical.SimplePeriodical;

public class Supplier<T extends Thing> extends SimplePeriodical implements Runnable {
	private Storage<T> storage;
	private static final long defaultPeriod = 1000; // 1 sec
	private Class<T> thingClass;
	private Thread thread;

	public Supplier(Storage<T> storage, Class<T> thingClass) {
		super(defaultPeriod);
		this.storage = storage;
		this.thingClass = thingClass;
		this.thread = new Thread(this);
	}

	@Override
	public void run() {
		while (true) {
			try {
				storage.store(thingClass.newInstance());
				Thread.sleep(getPeriod());
			} catch (InterruptedException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	public Thread getThread() {
		return thread;
	}
}
