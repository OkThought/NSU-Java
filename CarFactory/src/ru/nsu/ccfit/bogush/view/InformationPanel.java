package ru.nsu.ccfit.bogush.view;

import ru.nsu.ccfit.bogush.CarFactoryModel;

import javax.swing.*;

public class InformationPanel extends JPanel {
	public LabeledValue workers;
	public LabeledValue dealers;
	public LabeledValue accessoriesSuppliers;
	public LabeledValue carStorage;
	public LabeledValue engineStorage;
	public LabeledValue bodyStorage;
	public LabeledValue accessoriesStorage;
	public LabeledValue sold;
	public LabeledValue taskQueueSize;
	private JPanel panel;

	private static final String WORKERS_TEXT = "Workers: ";
	private static final String DEALERS_TEXT = "Dealers: ";
	private static final String ACCESSORIES_SUPPLIERS_TEXT = "Accessories Suppliers: ";
	private static final String CAR_STORAGE_TEXT = "Car Storage: ";
	private static final String ENGINE_STORAGE_TEXT = "Engine Storage: ";
	private static final String BODY_STORAGE_TEXT = "Body Storage: ";
	private static final String ACCESSORIES_STORAGE_TEXT = "Accessories Storage: ";
	private static final String SOLD_TEXT = "Sold: ";
	private static final String TASK_QUEUE_SIZE_TEXT = "Task queue size: ";

	private int workersCount;
	private int dealersCount;
	private int accessoriesSuppliersCount;

	private static final int CAR_STORAGE_INITIAL = 0;
	private static final int ENGINE_STORAGE_INITIAL = 0;
	private static final int BODY_STORAGE_INITIAL = 0;
	private static final int ACCESSORIES_STORAGE_INITIAL = 0;
	private static final int SOLD_INITIAL = 0;
	private static final int TASK_QUEUE_SIZE_INITIAL = 0;

	private int soldCount = SOLD_INITIAL;

	private CarFactoryModel model;

	public InformationPanel(CarFactoryModel model) {
		this.model = model;
		workersCount = model.getWorkersCount();
		dealersCount = model.getCarDealersCount();
		accessoriesSuppliersCount = model.getAccessorySuppliersCount();
	}

	private void createUIComponents() {
		workers = new LabeledValue(WORKERS_TEXT);
		dealers = new LabeledValue(DEALERS_TEXT);
		accessoriesSuppliers = new LabeledValue(ACCESSORIES_SUPPLIERS_TEXT);
		carStorage = new LabeledValue(CAR_STORAGE_TEXT);
		engineStorage = new LabeledValue(ENGINE_STORAGE_TEXT);
		bodyStorage = new LabeledValue(BODY_STORAGE_TEXT);
		accessoriesStorage = new LabeledValue(ACCESSORIES_STORAGE_TEXT);
		sold = new LabeledValue(SOLD_TEXT);
		taskQueueSize = new LabeledValue(TASK_QUEUE_SIZE_TEXT);

		workers.setValue(workersCount);
		dealers.setValue(dealersCount);
		accessoriesSuppliers.setValue(accessoriesSuppliersCount);

		carStorage.setValue(CAR_STORAGE_INITIAL);
		engineStorage.setValue(ENGINE_STORAGE_INITIAL);
		bodyStorage.setValue(BODY_STORAGE_INITIAL);
		accessoriesStorage.setValue(ACCESSORIES_STORAGE_INITIAL);
		sold.setValue(SOLD_INITIAL);
		taskQueueSize.setValue(TASK_QUEUE_SIZE_INITIAL);

		model.getStore().addCarSoldSubscriber(() -> sold.setValue(Integer.parseInt(sold.getText()) + 1));
		model.getEngineStorage().addSizeSubscriber(size -> engineStorage.setValue(size));
		model.getBodyStorage().addSizeSubscriber(size -> bodyStorage.setValue(size));
		model.getAccessoriesStorage().addSizeSubscriber(size -> accessoriesStorage.setValue(size));
		model.getCarStorage().addSizeSubscriber(size -> carStorage.setValue(size));
		model.getCarFactory().getThreadPool().addTaskQueueSizeSubscriber(size -> taskQueueSize.setValue(size));
	}
}
