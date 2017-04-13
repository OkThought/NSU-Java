package ru.nsu.ccfit.bogush.factory.thing.periodical;

public class SimplePeriodical implements Periodical {
	private long period;

	public SimplePeriodical(long period) {
		this.period = period;
	}

	@Override
	public void setPeriod(long millis) {
		if (millis >= 0) period = millis;
		else throw new IllegalArgumentException("period must be greater than or equal to 0");
	}

	@Override
	public long getPeriod() {
		return period;
	}
}
