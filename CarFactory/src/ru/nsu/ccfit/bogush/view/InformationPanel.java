package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.CarFactoryModel;

import javax.swing.*;

class InformationPanel extends JPanel {
	private LabeledValue workers;
	private LabeledValue dealers;
	private LabeledValue accessoriesSuppliers;
	private LabeledValue carStorage;
	private LabeledValue engineStorage;
	private LabeledValue bodyStorage;
	private LabeledValue accessoriesStorage;
	private LabeledValue sold;
	private LabeledValue taskQueueSize;
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

	private static final int CAR_STORAGE_INITIAL = 0;
	private static final int ENGINE_STORAGE_INITIAL = 0;
	private static final int BODY_STORAGE_INITIAL = 0;
	private static final int ACCESSORIES_STORAGE_INITIAL = 0;
	private static final int SOLD_INITIAL = 0;
	private static final int TASK_QUEUE_SIZE_INITIAL = 0;

	private int soldCount = SOLD_INITIAL;

	private static final String LOGGER_NAME = "InformationPanel";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public InformationPanel(CarFactoryModel model) {
		logger.traceEntry();
		int workersCount = model.getWorkersCount();
		int dealersCount = model.getCarDealersCount();
		int accessoriesSuppliersCount = model.getAccessorySuppliersCount();
		logger.trace("Initialize");
		logger.trace("workersCount = " + workersCount);
		logger.trace("dealersCount = " + dealersCount);
		logger.trace("accessoriesSuppliersCount = " + accessoriesSuppliersCount);

		workers.setValue(workersCount);
		dealers.setValue(dealersCount);
		accessoriesSuppliers.setValue(accessoriesSuppliersCount);

		carStorage.setValue(CAR_STORAGE_INITIAL);
		engineStorage.setValue(ENGINE_STORAGE_INITIAL);
		bodyStorage.setValue(BODY_STORAGE_INITIAL);
		accessoriesStorage.setValue(ACCESSORIES_STORAGE_INITIAL);
		sold.setValue(SOLD_INITIAL);
		taskQueueSize.setValue(TASK_QUEUE_SIZE_INITIAL);

		model.getStore().addCarSoldSubscriber(count -> sold.setValue(count));
		model.getEngineStorage().addSizeSubscriber(size -> engineStorage.setValue(size));
		model.getBodyStorage().addSizeSubscriber(size -> bodyStorage.setValue(size));
		model.getAccessoriesStorage().addSizeSubscriber(size -> accessoriesStorage.setValue(size));
		model.getCarStorage().addSizeSubscriber(size -> carStorage.setValue(size));
		model.getCarFactory().getThreadPool().addTaskQueueSizeSubscriber(size -> taskQueueSize.setValue(size));
		logger.traceExit();
	}

	private void createUIComponents() {
		logger.traceEntry();
		workers = new LabeledValue(WORKERS_TEXT);
		dealers = new LabeledValue(DEALERS_TEXT);
		accessoriesSuppliers = new LabeledValue(ACCESSORIES_SUPPLIERS_TEXT);
		carStorage = new LabeledValue(CAR_STORAGE_TEXT);
		engineStorage = new LabeledValue(ENGINE_STORAGE_TEXT);
		bodyStorage = new LabeledValue(BODY_STORAGE_TEXT);
		accessoriesStorage = new LabeledValue(ACCESSORIES_STORAGE_TEXT);
		sold = new LabeledValue(SOLD_TEXT);
		taskQueueSize = new LabeledValue(TASK_QUEUE_SIZE_TEXT);
		logger.traceExit();
	}
}
