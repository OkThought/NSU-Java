package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;
import java.util.List;

public class CarStore {
	private Dealer[] dealers;
	private Thread[] dealerThreads;
	private List<CarSoldSubscriber> carSoldSubscribers = new ArrayList<>();

	private static final String LOGGER_NAME = "Store";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	private static final Marker OFF_MARKER = MarkerManager.getMarker("OFF");
	private static final Marker CAR_SOLD_MARKER = MarkerManager.getMarker("SOLD");
	private Marker carSoldMarker;

	private static final long DEFAULT_PERIOD = 0;

	public CarStore(CarStorage storage, int amountOfDealers) {
		this(storage, amountOfDealers, DEFAULT_PERIOD);
	}

	public CarStore(CarStorage storage, int amountOfDealers, long period) {
		this.dealers = new Dealer[amountOfDealers];
		this.dealerThreads = new Thread[amountOfDealers];
		for (int i = 0; i < amountOfDealers; ++i) {
			Dealer dealer = new Dealer(storage, this, period);
			dealers[i] = dealer;
			dealerThreads[i] = dealer.thread;
		}
	}

	public void setLoggingSales(boolean loggingSales) {
		this.carSoldMarker = loggingSales ? CAR_SOLD_MARKER: OFF_MARKER;
	}

	public boolean isLoggingSales() {
		return carSoldMarker == CAR_SOLD_MARKER;
	}

	public Dealer[] getDealers() {
		return dealers;
	}

	public void setPeriod(long period) {
		logger.trace("set period " + period + " milliseconds");
		for (Dealer dealer: dealers) {
			dealer.setPeriod(period);
		}
	}

	public void start() {
		for (Thread dealerThread: dealerThreads) {
			logger.trace("start " + dealerThread.getName());
			dealerThread.start();
		}
	}

	public void stop() {
		for (Thread dealerThread: dealerThreads) {
			logger.trace("stop " + dealerThread.getName());
			dealerThread.interrupt();
		}
	}

	private void sell(Car car) {
		logger.trace(CAR_SOLD_MARKER, "sold " + car.getInfo());
		for (CarSoldSubscriber subscriber: carSoldSubscribers) {
			subscriber.carSold();
		}
	}

	public void addCarSoldSubscriber(CarSoldSubscriber subscriber) {
		logger.trace("add CarSoldSubscriber " + subscriber.getClass().getSimpleName());
		carSoldSubscribers.add(subscriber);
	}

	public static class Dealer extends SimplePeriodical implements Runnable {
		private CarStorage storage;
		private CarStore store;
		private Thread thread;

		private static final String LOGGER_NAME = "Dealer";
		private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

		private Dealer(CarStorage storage, CarStore store, long period) {
			super(period);
			logger.trace("initialize with period " + period);
			this.storage = storage;
			this.store = store;
			this.thread = new Thread(this);
			thread.setName(toString());
		}

		@Override
		public void run() {
			try {
				while(true) {
					logger.debug("request car from storage");
					Car car = storage.take();
					logger.debug(car + " taken");
					store.sell(car);
					waitPeriod();
				}
			} catch (InterruptedException e) {
				logger.trace("interrupted");
			} finally {
				logger.trace("stopped");
			}
		}

		@Override
		public String toString() {
			return this.getClass().getSimpleName() + "-" + thread.getId();
		}

		public Thread getThread() {
			return thread;
		}
	}

	public interface CarSoldSubscriber {
		void carSold();
	}
}
