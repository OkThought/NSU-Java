package ru.nsu.ccfit.bogush.factory;

public class Thing extends SimplyNamed implements Identifiable {
	private final long id;

	public Thing() {
		this.id = Things.getNewId();
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return super.toString() + "-" + String.valueOf(getId());
	}
}
