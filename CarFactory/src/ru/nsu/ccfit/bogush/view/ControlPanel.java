package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.CarFactoryModel;
import ru.nsu.ccfit.bogush.factory.Supplier;

import javax.swing.*;

class ControlPanel extends JPanel {
	private LabeledSliderWithTextField enginePeriod;
	private LabeledSliderWithTextField bodyPeriod;
	private LabeledSliderWithTextField accessoriesPeriod;
	private LabeledSliderWithTextField dealersPeriod;
	private JPanel panel;

	private static final String ENGINE_PERIOD_LABEL_TEXT = "Engine Supplier Period";
	private static final String BODY_PERIOD_LABEL_TEXT = "Body Supplier Period";
	private static final String ACCESSORIES_PERIOD_LABEL_TEXT = "Accessories Supplier Period";
	private static final String DEALERS_PERIOD_LABEL_TEXT = "Dealers Supplier Period";
	private static final int MIN_PERIOD = 0;

	private static final int MAX_PERIOD = 10000;
	private static final int INITIAL_PERIOD = 0;
	private static final int INTERVAL = 1;

	private final CarFactoryModel model;

	private static final String LOGGER_NAME = "ControlPanel";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public ControlPanel(CarFactoryModel model) {
		this.model = model;
		logger.traceEntry();
		logger.traceExit();
	}

	private void createUIComponents() {
		logger.traceEntry();
		enginePeriod = new LabeledSliderWithTextField(ENGINE_PERIOD_LABEL_TEXT, MIN_PERIOD, MAX_PERIOD, INTERVAL);
		bodyPeriod = new LabeledSliderWithTextField(BODY_PERIOD_LABEL_TEXT, MIN_PERIOD, MAX_PERIOD, INTERVAL);
		accessoriesPeriod = new LabeledSliderWithTextField(ACCESSORIES_PERIOD_LABEL_TEXT, MIN_PERIOD, MAX_PERIOD, INTERVAL);
		dealersPeriod = new LabeledSliderWithTextField(DEALERS_PERIOD_LABEL_TEXT, MIN_PERIOD, MAX_PERIOD, INTERVAL);

		reset();

		enginePeriod.addValueChangeListener(value -> model.getEngineSupplier().setPeriod(value));

		bodyPeriod.addValueChangeListener(value -> model.getBodySupplier().setPeriod(value));

		accessoriesPeriod.addValueChangeListener(value -> {
			for (Supplier supplier : model.getAccessorySuppliers())
				supplier.setPeriod(value);
		});

		dealersPeriod.addValueChangeListener(value -> model.getStore().setPeriod(value));

		model.getStore().setPeriod(dealersPeriod.getValue());
		model.getBodySupplier().setPeriod(bodyPeriod.getValue());
		model.getEngineSupplier().setPeriod(enginePeriod.getValue());
		for (Supplier supplier : model.getAccessorySuppliers()) {
			supplier.setPeriod(enginePeriod.getValue());
		}
		logger.traceExit();
	}

	void reset() {
		logger.traceEntry();
		enginePeriod.setValue(INITIAL_PERIOD);
		bodyPeriod.setValue(INITIAL_PERIOD);
		accessoriesPeriod.setValue(INITIAL_PERIOD);
		dealersPeriod.setValue(INITIAL_PERIOD);
		logger.traceExit();
	}
}
