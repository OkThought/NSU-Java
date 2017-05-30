package ru.nsu.ccfit.bogush.factory;

public class CarFactoryObject extends SimplyNamed implements Identifiable {
	private final long id;

	CarFactoryObject() {
		this.id = Thread.currentThread().getId() * 13 + this.hashCode();
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
