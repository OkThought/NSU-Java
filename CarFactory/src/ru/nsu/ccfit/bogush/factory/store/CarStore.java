package ru.nsu.ccfit.bogush.factory.store;

import ru.nsu.ccfit.bogush.factory.storage.CarStorage;
import ru.nsu.ccfit.bogush.factory.thing.Car;

import java.util.ArrayList;
import java.util.List;

public class CarStore {
	private CarStorage storage;
	private CarDealer[] dealers;
	private List<CarSellSubscriber> carSellSubscribers = new ArrayList<>();

	public CarStore(CarStorage storage, int amountOfDealers) {
		this.storage = storage;
		this.dealers = new CarDealer[amountOfDealers];
		for (int i = 0; i < amountOfDealers; ++i) {
			CarDealer dealer = new CarDealer(storage, this);
			dealers[i] = dealer;
		}
	}

	public void start() {
		for (CarDealer dealer: dealers) {
			dealer.getThread().start();
		}
	}

	public void sell(Car car) {
//		System.out.println("Sell a car with id: " + car.getId());
		for (CarSellSubscriber subscriber: carSellSubscribers) {
			subscriber.carSold();
		}
	}

	public CarDealer[] getDealers() {
		return dealers;
	}

	public void subscribe(CarSellSubscriber subscriber) {
		carSellSubscribers.add(subscriber);
	}
}
