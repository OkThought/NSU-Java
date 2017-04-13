package ru.nsu.ccfit.bogush.factory.thing;

public class Car extends Thing {
	private final CarEngine engine;
	private final CarBody body;
	private final CarAccessory[] accessories;
	public static final int ACCESSORY_NUMBER = 4;

	public Car(CarEngine engine, CarBody body, CarAccessory[] accessories) {
		this.engine = engine;
		this.body = body;
		if (accessories.length != ACCESSORY_NUMBER)
			throw new IllegalArgumentException("Amount of accessories should be " + ACCESSORY_NUMBER);
		this.accessories = accessories;
	}

//	@Override
//	public String toString() {
//		return "Car" + getId();
//	}
}
