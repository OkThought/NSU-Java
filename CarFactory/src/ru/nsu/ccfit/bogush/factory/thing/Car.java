package ru.nsu.ccfit.bogush.factory.thing;

public class Car extends Thing {
	private final Engine engine;
	private final Body body;
	private final Accessories accessories;

	public Car(Engine engine, Body body, Accessories accessories) {
		this.engine = engine;
		this.body = body;
		this.accessories = accessories;
	}

	public static class Accessories extends Thing {}

	public static class Body extends Thing {}

	public static class Engine extends Thing {}
}
