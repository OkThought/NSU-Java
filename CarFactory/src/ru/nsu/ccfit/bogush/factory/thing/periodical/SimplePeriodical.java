package ru.nsu.ccfit.bogush.factory.thing.periodical;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimplePeriodical implements Periodical {
	private long period;

	private static final String LOGGER_NAME = "SimplePeriodical";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public SimplePeriodical(long period) {
		this.period = period;
	}

	private final Object lock = new Object();

	@Override
	public void setPeriod(long period) {
		logger.trace("set period " + period + " milliseconds");
		if (period < 0) {
			logger.error("period < 0");
			throw new IllegalArgumentException("period must be greater than or equal to 0");
		}
		this.period = period;
	}

	@Override
	public void waitPeriod() {
		long timeToWait = period;
		long time = System.currentTimeMillis();
		try {
			synchronized (lock) {
				while (timeToWait > 0) {
					lock.wait(timeToWait);
					long newTime = System.currentTimeMillis();
					timeToWait -= newTime - time;
					time = newTime;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error(e);
			System.exit(1);
		}
	}

	@Override
	public long getPeriod() {
		return period;
	}
}
