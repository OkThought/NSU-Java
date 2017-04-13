package ru.nsu.ccfit.bogush.factory.thing;

public class Things {
	private long count = 0;
	private static Things instance;

	private Things() {}

	private synchronized long getId() {
		return count++;
	}

	private static Things getInstance() {
		if (instance == null) instance = new Things();
		return instance;
	}

	public static long getNewId() {
		return getInstance().getId();
	}
}
