package ru.nsu.ccfit.bogush.view;

import ru.nsu.ccfit.bogush.factory.*;
import ru.nsu.ccfit.bogush.factory.storage.StorageSizeSubscriber;
import ru.nsu.ccfit.bogush.factory.store.CarDealer;
import ru.nsu.ccfit.bogush.factory.store.CarSellSubscriber;
import ru.nsu.ccfit.bogush.factory.store.CarStore;
import ru.nsu.ccfit.bogush.factory.thing.CarAccessory;
import ru.nsu.ccfit.bogush.factory.thing.CarBody;
import ru.nsu.ccfit.bogush.factory.thing.CarEngine;
import ru.nsu.ccfit.bogush.threadpool.TaskSubscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FactoryView implements CarSellSubscriber, TaskSubscriber, StorageSizeSubscriber, ActionListener {
	private JPanel mainPanel;
	private JPanel controlPanel;
	private JPanel infoPanel;

	private JSlider carDealerPeriodSlider;
	private JSlider engineSupplierPeriodSlider;
	private JSlider bodySupplierPeriodSlider;
	private JSlider accessorySuppliersPeriodSlider;

	private int carDealerInitialPeriod;
	private int engineSupplierInitialPeriod;
	private int bodySupplierInitialPeriod;
	private int accessorySuppliersInitialPeriod;

	private JLabel carsSoldLabel;
	private JLabel tasksAwaitingLabel;

	private JButton resetButton;
	private JButton startButton;
	private JLabel carEngineStorageSizeLabel;
	private JLabel carBodyStorageSizeLabel;
	private JLabel carAccessoryStorageSizeLabel;
	private JLabel carStorageSizeLabel;
	private JLabel accessorySuppliersLabel;
	private JLabel dealersLabel;
	private JLabel workersLabel;

	private CarFactory factory;
	private CarStore carStore;
	private Supplier<CarEngine> carEngineSupplier;
	private Supplier<CarBody> carBodySupplier;
	private Supplier<CarAccessory>[] carAccessorySuppliers;

	private String carsSoldLabelInitialText;
	private String tasksAwaitingLabelInitialText;

	private String carEngineStorageSizeLabelInitialText;
	private String carBodyStorageSizeLabelInitialText;
	private String carAccessoryStorageSizeLabelInitialText;
	private String carStorageSizeLabelInitialText;

	private static final int PERIOD_MULTIPLIER = 1000; // secs to millis

	private int carsSold = 0;

	public FactoryView(CarFactory factory, CarStore carStore, Supplier<CarEngine> carEngineSupplier,
	                   Supplier<CarBody> carBodySupplier, Supplier<CarAccessory>[] carAccessorySuppliers) {
		this.factory = factory;
		this.carStore = carStore;
		this.carEngineSupplier = carEngineSupplier;
		this.carBodySupplier = carBodySupplier;
		this.carAccessorySuppliers = carAccessorySuppliers;

		factory.getCarEngineStorage().subscribe(this);
		factory.getCarBodyStorage().subscribe(this);
		factory.getCarAccessoryStorage().subscribe(this);
		factory.getCarStorage().subscribe(this);
		factory.getThreadPool().subscribe(this);
		carStore.subscribe(this);

		createFrame();

		resetButton.setActionCommand("reset");
		startButton.setActionCommand("start");

		resetButton.addActionListener(this);
		startButton.addActionListener(this);

		manageLabels();
		manageSliders();
	}

	private void createFrame() {
		JFrame frame = new JFrame("Car Factory");
		frame.setContentPane(mainPanel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}

	private void manageLabels() {
		workersLabel.setText(workersLabel.getText() + factory.getThreadPool().size());
		dealersLabel.setText(dealersLabel.getText() + carStore.getDealers().length);
		accessorySuppliersLabel.setText(accessorySuppliersLabel.getText() + carAccessorySuppliers.length);

		carsSoldLabelInitialText = carsSoldLabel.getText().trim() + " ";
		tasksAwaitingLabelInitialText = tasksAwaitingLabel.getText().trim() + " ";
		carEngineStorageSizeLabelInitialText = carEngineStorageSizeLabel.getText().trim() + " ";
		carBodyStorageSizeLabelInitialText = carBodyStorageSizeLabel.getText().trim() + " ";
		carAccessoryStorageSizeLabelInitialText = carAccessoryStorageSizeLabel.getText().trim() + " ";
		carStorageSizeLabelInitialText = carStorageSizeLabel.getText().trim() + " ";

		carsSoldLabel.setText(carsSoldLabelInitialText + 0);
		tasksAwaitingLabel.setText(tasksAwaitingLabelInitialText + 0);
		carEngineStorageSizeLabel.setText(carEngineStorageSizeLabelInitialText + 0);
		carBodyStorageSizeLabel.setText(carBodyStorageSizeLabelInitialText + 0);
		carAccessoryStorageSizeLabel.setText(carAccessoryStorageSizeLabelInitialText + 0);
		carStorageSizeLabel.setText(carStorageSizeLabelInitialText + 0);
	}

	private void manageSliders() {
		carDealerPeriodSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		engineSupplierPeriodSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bodySupplierPeriodSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		accessorySuppliersPeriodSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		carDealerInitialPeriod = carDealerPeriodSlider.getValue();
		engineSupplierInitialPeriod = engineSupplierPeriodSlider.getValue();
		bodySupplierInitialPeriod = bodySupplierPeriodSlider.getValue();
		accessorySuppliersInitialPeriod = accessorySuppliersPeriodSlider.getValue();

		carDealerPeriodSlider.addChangeListener(e -> {
			long period = carDealerPeriodSlider.getValue() * PERIOD_MULTIPLIER;
			for (CarDealer dealer: carStore.getDealers()) {
				dealer.setPeriod(period);
			}
		});

		engineSupplierPeriodSlider.addChangeListener(e -> {
			carEngineSupplier.setPeriod(engineSupplierPeriodSlider.getValue() * PERIOD_MULTIPLIER);
		});

		bodySupplierPeriodSlider.addChangeListener(e -> {
			carBodySupplier.setPeriod(bodySupplierPeriodSlider.getValue() * PERIOD_MULTIPLIER);
		});

		accessorySuppliersPeriodSlider.addChangeListener(e -> {
			long period = accessorySuppliersPeriodSlider.getValue() * PERIOD_MULTIPLIER;
			for (Supplier<CarAccessory> supplier: carAccessorySuppliers) {
				supplier.setPeriod(period);
			}
		});
	}

	@Override
	public void carSold() {
		carsSoldLabel.setText(carsSoldLabelInitialText + (++carsSold));
	}

	@Override
	public void queueSizeChanged(int tasksAwaiting) {
		tasksAwaitingLabel.setText(tasksAwaitingLabelInitialText + tasksAwaiting);
	}

	@Override
	public void sizeChanged() {
		carEngineStorageSizeLabel.setText(carEngineStorageSizeLabelInitialText + factory.getCarEngineStorage().size());
		carBodyStorageSizeLabel.setText(carBodyStorageSizeLabelInitialText + factory.getCarBodyStorage().size());
		carAccessoryStorageSizeLabel.setText(carAccessoryStorageSizeLabelInitialText + factory.getCarAccessoryStorage().size());
		carStorageSizeLabel.setText(carStorageSizeLabelInitialText + factory.getCarStorage().size());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("reset".equals(e.getActionCommand())) {
			carDealerPeriodSlider.setValue(carDealerInitialPeriod);
			engineSupplierPeriodSlider.setValue(engineSupplierInitialPeriod);
			bodySupplierPeriodSlider.setValue(bodySupplierInitialPeriod);
			accessorySuppliersPeriodSlider.setValue(accessorySuppliersInitialPeriod);
		} else if ("start".equals(e.getActionCommand())) {
			startButton.setEnabled(false);
			new Thread(factory.getCarStorage().getController()).start();
			carStore.start();
			carEngineSupplier.getThread().start();
			carBodySupplier.getThread().start();
			for (Supplier<CarAccessory> supplier: carAccessorySuppliers) {
				supplier.getThread().start();
			}
		}
	}
}
