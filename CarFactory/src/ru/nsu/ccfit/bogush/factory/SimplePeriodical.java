package ru.nsu.ccfit.bogush.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.Pauser;

public class SimplePeriodical implements Periodical, Pauser.Pausable {
	private long period;
	private boolean periodChanged = true;
	private boolean paused = false;

	private static final String LOGGER_NAME = "SimplePeriodical";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public SimplePeriodical(long period) {
		this.period = period;
	}

	private final Object lock = new Object();

	@Override
	public void setPeriod(long period) {
		logger.trace("set period " + period + " milliseconds to " + this);
		if (period < 0) {
			logger.error("period < 0");
			throw new IllegalArgumentException("period must be greater than or equal to 0");
		}
		this.period = period;
		synchronized (lock) {
			periodChanged = true;
			lock.notifyAll();
		}
	}

	@Override
	public void waitPeriod() throws InterruptedException {
		logger.traceEntry();
		long timeToWait = period;
		logger.trace("begin waiting for " + timeToWait + " millis");
		synchronized (lock) {
			while (timeToWait > 0) {
				long time = System.currentTimeMillis();
				lock.wait(timeToWait);
				if (periodChanged) {
					logger.trace("period was changed while waiting");
					periodChanged = false;
					return;
				} else {
					timeToWait -= System.currentTimeMillis() - time;
				}
				while (paused) {
					lock.wait();
				}
			}
			while (paused) {
				lock.wait();
			}
		}
		logger.traceExit();
	}

	@Override
	public void pause() {
		logger.traceEntry();
		if (!paused) {
			paused = true;
			synchronized (lock) {
				lock.notifyAll();
			}
		} else {
			logger.trace("already paused");
		}
		logger.traceExit();
	}

	@Override
	public void resume() {
		logger.traceEntry();
		if (paused) {
			paused = false;
			synchronized (lock) {
				lock.notifyAll();
			}
		} else {
			logger.trace("wasn't paused");
		}
		logger.traceExit();
	}

	@Override
	public boolean isPaused() {
		logger.traceEntry();
		return logger.traceExit(paused);
	}

	@Override
	public long getPeriod() {
		logger.traceEntry();
		return logger.traceExit(period);
	}
}
