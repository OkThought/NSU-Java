package ru.nsu.ccfit.bogush.view;

import ru.nsu.ccfit.bogush.CarFactoryModel;
import ru.nsu.ccfit.bogush.factory.store.CarSellSubscriber;
import ru.nsu.ccfit.bogush.threadpool.TaskSubscriber;

import javax.swing.*;

public class InformationPanel extends JPanel implements TaskSubscriber, CarSellSubscriber {
	public LabeledTextField workers;
	public LabeledTextField dealers;
	public LabeledTextField accessoriesSuppliers;
	public LabeledTextField carStorage;
	public LabeledTextField engineStorage;
	public LabeledTextField bodyStorage;
	public LabeledTextField accessoriesStorage;
	public LabeledTextField sold;
	public LabeledTextField awaiting;
	private JPanel panel;

	private static final String WORKERS_TEXT = "Workers: ";
	private static final String DEALERS_TEXT = "Dealers: ";
	private static final String ACCESSORIES_SUPPLIERS_TEXT = "Accessories Suppliers: ";
	private static final String CAR_STORAGE_TEXT = "Car Storage: ";
	private static final String ENGINE_STORAGE_TEXT = "Engine Storage: ";
	private static final String BODY_STORAGE_TEXT = "Body Storage: ";
	private static final String ACCESSORIES_STORAGE_TEXT = "Accessories Storage: ";
	private static final String SOLD_TEXT = "Sold: ";
	private static final String AWAITING_TEXT = "Awaiting: ";

	private int workersCount;
	private int dealersCount;
	private int accessoriesSuppliersCount;

	private static final int CAR_STORAGE_INITIAL = 0;
	private static final int ENGINE_STORAGE_INITIAL = 0;
	private static final int BODY_STORAGE_INITIAL = 0;
	private static final int ACCESSORIES_STORAGE_INITIAL = 0;
	private static final int SOLD_INITIAL = 0;
	private static final int AWAITING_INITIAL = 0;

	private int soldCount = SOLD_INITIAL;

	private CarFactoryModel model;

	public InformationPanel(CarFactoryModel model) {
		this.model = model;
		workersCount = model.workersCount;
		dealersCount = model.carDealersCount;
		accessoriesSuppliersCount = model.accessorySuppliersCount;
	}

	@Override
	public void queueSizeChanged(int tasksAwaiting) {
		awaiting.setValue(tasksAwaiting);
	}

	@Override
	public void carSold() {
		soldCount++;
		sold.setValue(soldCount);
	}

	private void createUIComponents() {
		workers = new LabeledTextField(WORKERS_TEXT, false);
		dealers = new LabeledTextField(DEALERS_TEXT, false);
		accessoriesSuppliers = new LabeledTextField(ACCESSORIES_SUPPLIERS_TEXT, false);
		carStorage = new LabeledTextField(CAR_STORAGE_TEXT, false);
		engineStorage = new LabeledTextField(ENGINE_STORAGE_TEXT, false);
		bodyStorage = new LabeledTextField(BODY_STORAGE_TEXT, false);
		accessoriesStorage = new LabeledTextField(ACCESSORIES_STORAGE_TEXT, false);
		sold = new LabeledTextField(SOLD_TEXT, false);
		awaiting = new LabeledTextField(AWAITING_TEXT, false);

		workers.setValue(workersCount);
		dealers.setValue(dealersCount);
		accessoriesSuppliers.setValue(accessoriesSuppliersCount);

		carStorage.setValue(CAR_STORAGE_INITIAL);
		engineStorage.setValue(ENGINE_STORAGE_INITIAL);
		bodyStorage.setValue(BODY_STORAGE_INITIAL);
		accessoriesStorage.setValue(ACCESSORIES_STORAGE_INITIAL);
		sold.setValue(SOLD_INITIAL);
		awaiting.setValue(AWAITING_INITIAL);

		model.engineStorage.subscribe(size -> engineStorage.setValue(size));

		model.bodyStorage.subscribe(size -> bodyStorage.setValue(size));

		model.accessoriesStorage.subscribe(size -> accessoriesStorage.setValue(size));

		model.carStorage.subscribe(size -> carStorage.setValue(size));

		model.carFactory.getThreadPool().subscribe(tasksAwaiting -> awaiting.setValue(tasksAwaiting));
	}
}
