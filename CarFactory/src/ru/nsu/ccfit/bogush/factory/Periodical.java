package ru.nsu.ccfit.bogush.factory;

interface Periodical {
	void setPeriod(long millis);
	long getPeriod();
	void waitPeriod() throws InterruptedException;
}
