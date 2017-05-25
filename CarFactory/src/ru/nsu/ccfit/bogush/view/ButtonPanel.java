package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.CarFactoryModel;
import ru.nsu.ccfit.bogush.Pauser;
import ru.nsu.ccfit.bogush.factory.Supplier;

import javax.swing.*;
import java.util.Arrays;

public class ButtonPanel extends JComponent {
	private static final String START = "start";
	private static final String PAUSE = "pause";
	private static final String RESUME = "resume";
	private JButton startButton;
	private JButton resetButton;
	private JPanel panel;
	private JCheckBox logSalesCheckBox;
	private CarFactoryModel model;
	private Pauser pauser;

	private static final String LOGGER_NAME = "ButtonPanel";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public ButtonPanel(CarFactoryModel model) {
		this.model = model;
		startButton.setActionCommand(START);
		startButton.addActionListener(e -> {
			logger.trace("Start button: action command '" + e.getActionCommand() + '\'');
			if (START.equals(e.getActionCommand())) {
				startButton.setActionCommand(PAUSE);
				startButton.setText("Pause");

				Thread thread;
				thread = model.getEngineSupplier().getThread();
				logger.trace("start " + thread.getName());
				thread.start();
				thread = model.getBodySupplier().getThread();
				logger.trace("start " + thread.getName());
				thread.start();
				for (Supplier supplier: model.getAccessorySuppliers()) {
					thread = supplier.getThread();
					logger.trace("start " + thread.getName());
					thread.start();
				}
				model.getStore().start();
				model.getCarFactory().getThreadPool().start();
				model.getCarStorageController().start();
			} else if (PAUSE.equals(e.getActionCommand())) {
				startButton.setActionCommand(RESUME);
				startButton.setText("Resume");
				pauser.pause();
			} else if (RESUME.equals(e.getActionCommand())) {
				startButton.setActionCommand(PAUSE);
				startButton.setText("Pause");
				pauser.resume();
			} else {
				logger.error("Shouldn't get here");
				System.exit(1);
			}
		});

		resetButton.addActionListener(e -> {
			// TODO: reset
		});

		logSalesCheckBox.setSelected(model.isLoggingSales());

		logSalesCheckBox.addActionListener(e -> {
			logger.trace("logSales checkbox action");
			model.toggleLoggingSales();
		});

		pauser = new Pauser();
		pauser.addPausable(model.getBodySupplier());
		pauser.addPausable(model.getEngineSupplier());
		pauser.addPausables(Arrays.asList(model.getAccessorySuppliers()));
		pauser.addPausables(Arrays.asList(model.getStore().getDealers()));
	}
}
