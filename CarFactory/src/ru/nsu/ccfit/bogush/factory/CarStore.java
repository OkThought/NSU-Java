package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;
import java.util.List;

public class CarStore {
	private final Dealer[] dealers;
	private final Thread[] dealerThreads;
	private final List<CarSoldSubscriber> carSoldSubscribers = new ArrayList<>();
	private int carsSoldCount = 0;

	private static final String LOGGER_NAME = "Store";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	private static final String CAR_SOLD_LOGGER_NAME = "CarSoldLogger";
	private static final Logger carSoldLogger = LogManager.getLogger(CAR_SOLD_LOGGER_NAME);

	private static final Marker OFF_MARKER = MarkerManager.getMarker("OFF");
	private static final Marker CAR_SOLD_MARKER = MarkerManager.getMarker("SOLD");
	private Marker carSoldMarker;

	private static final long DEFAULT_PERIOD = 0;

	public CarStore(CarStorage storage, int amountOfDealers) {
		this(storage, amountOfDealers, DEFAULT_PERIOD);
	}

	private CarStore(CarStorage storage, int amountOfDealers, long period) {
		logger.traceEntry();
		this.dealers = new Dealer[amountOfDealers];
		this.dealerThreads = new Thread[amountOfDealers];
		for (int i = 0; i < amountOfDealers; ++i) {
			Dealer dealer = new Dealer(storage, this, period);
			dealers[i] = dealer;
			dealerThreads[i] = dealer.thread;
		}
		logger.traceExit();
	}

	public void setPeriod(long period) {
		logger.traceEntry();
		logger.trace("set period " + period + " milliseconds");
		for (Dealer dealer: dealers) {
			dealer.setPeriod(period);
		}
		logger.traceExit();
	}

	public void start() {
		logger.traceEntry();
		for (Thread dealerThread: dealerThreads) {
			logger.trace("start " + dealerThread.getName());
			dealerThread.start();
		}
		logger.traceExit();
	}

	public void stop() {
		logger.traceEntry();
		for (Thread dealerThread: dealerThreads) {
			logger.trace("stop " + dealerThread.getName());
			dealerThread.interrupt();
		}
		logger.traceExit();
	}

	private synchronized void sell(Car car) {
		logger.traceEntry();
		carSoldLogger.info(carSoldMarker, "sold " + car.getInfo());
		carSoldLogger.debug("marker: {}", carSoldMarker);

		carsSoldCount++;
		for (CarSoldSubscriber subscriber: carSoldSubscribers) {
			subscriber.carSoldCountChanged(carsSoldCount);
		}
		logger.traceExit();
	}

	public void addCarSoldSubscriber(CarSoldSubscriber subscriber) {
		logger.traceEntry();
		logger.trace("add CarSoldSubscriber " + subscriber.getClass().getSimpleName());
		carSoldSubscribers.add(subscriber);
		logger.traceExit();
	}

	public void setLoggingSales(boolean loggingSales) {
		logger.traceEntry();
		this.carSoldMarker = loggingSales ? CAR_SOLD_MARKER: OFF_MARKER;
		logger.traceExit();
	}

	public boolean isLoggingSales() {
		logger.traceEntry();
		return logger.traceExit(carSoldMarker == CAR_SOLD_MARKER);
	}

	public Dealer[] getDealers() {
		logger.traceEntry();
		return logger.traceExit(dealers);
	}

	public static class Dealer extends SimplePeriodical implements Runnable {
		private CarStorage storage;
		private CarStore store;
		private final Thread thread;

		private static final String LOGGER_NAME = "Dealer";
		private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

		private Dealer(CarStorage storage, CarStore store, long period) {
			super(period);
			logger.traceEntry();
			logger.trace("initialize with period " + period);
			this.storage = storage;
			this.store = store;
			this.thread = new Thread(this);
			thread.setName(toString());
			logger.traceExit();
		}

		@Override
		public void run() {
			logger.traceEntry();
			try {
				while(!Thread.interrupted()) {
					logger.trace("request car from storage");
					Car car = storage.take();
					logger.trace(car + " taken");
					store.sell(car);
					waitPeriod();
				}
			} catch (InterruptedException e) {
				logger.trace("interrupted");
			} finally {
				logger.trace("stopped");
				logger.traceExit();
			}
		}

		@Override
		public String toString() {
			return this.getClass().getSimpleName() + "-" + thread.getId();
		}

		public Thread getThread() {
			logger.traceEntry();
			return logger.traceExit(thread);
		}
	}

	public interface CarSoldSubscriber {
		void carSoldCountChanged(int count);
	}
}
