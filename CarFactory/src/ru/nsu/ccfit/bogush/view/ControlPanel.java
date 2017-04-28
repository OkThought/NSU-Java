package ru.nsu.ccfit.bogush.view;

import ru.nsu.ccfit.bogush.CarFactoryModel;
import ru.nsu.ccfit.bogush.factory.Supplier;
import ru.nsu.ccfit.bogush.factory.store.CarDealer;

import javax.swing.*;

public class ControlPanel extends JPanel {
	public LabeledSliderWithTextField enginePeriod;
	public LabeledSliderWithTextField bodyPeriod;
	public LabeledSliderWithTextField accessoriesPeriod;
	public LabeledSliderWithTextField dealersPeriod;
	private JPanel panel;

	private static final String ENGINE_PERIOD_LABEL_TEXT = "Engine Supplier Period";
	private static final String BODY_PERIOD_LABEL_TEXT = "Body Supplier Period";
	private static final String ACCESSORIES_PERIOD_LABEL_TEXT = "Accessories Supplier Period";
	private static final String DEALERS_PERIOD_LABEL_TEXT = "Dealers Supplier Period";
	private static final int MIN_PERIOD = 0;

	private static final int MAX_PERIOD = 10000;
	private static final int INITIAL_PERIOD = 1;
	private static final int INTERVAL = 1;

	private CarFactoryModel model;

	public ControlPanel(CarFactoryModel model) {
		this.model = model;
	}

	private void createUIComponents() {
		enginePeriod = new LabeledSliderWithTextField(ENGINE_PERIOD_LABEL_TEXT, MIN_PERIOD, MAX_PERIOD, INTERVAL);
		bodyPeriod = new LabeledSliderWithTextField(BODY_PERIOD_LABEL_TEXT, MIN_PERIOD, MAX_PERIOD, INTERVAL);
		accessoriesPeriod = new LabeledSliderWithTextField(ACCESSORIES_PERIOD_LABEL_TEXT, MIN_PERIOD, MAX_PERIOD, INTERVAL);
		dealersPeriod = new LabeledSliderWithTextField(DEALERS_PERIOD_LABEL_TEXT, MIN_PERIOD, MAX_PERIOD, INTERVAL);

		enginePeriod.setValue(INITIAL_PERIOD);
		bodyPeriod.setValue(INITIAL_PERIOD);
		accessoriesPeriod.setValue(INITIAL_PERIOD);
		dealersPeriod.setValue(INITIAL_PERIOD);

		enginePeriod.addListener(value -> model.getEngineSupplier().setPeriod(value));

		bodyPeriod.addListener(value -> model.getBodySupplier().setPeriod(value));

		accessoriesPeriod.addListener(value -> {
			for (Supplier supplier: model.getAccessorySuppliers())
				supplier.setPeriod(value);
		});

		dealersPeriod.addListener(value -> {
			for (CarDealer dealer: model.getDealers()) {
				dealer.setPeriod(value);
			}
		});
	}
}
