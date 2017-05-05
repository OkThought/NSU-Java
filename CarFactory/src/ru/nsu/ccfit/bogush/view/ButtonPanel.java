package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.CarFactoryModel;
import ru.nsu.ccfit.bogush.factory.Supplier;

import javax.swing.*;

public class ButtonPanel extends JComponent {
	private static final String START = "start";
	private static final String PAUSE = "pause";
	private static final String RESET = "reset";
	private static final String CONTINUE = "continue";
	private JButton startButton;
	private JButton resetButton;
	private JPanel panel;
	private JCheckBox logSalesCheckBox;
	private CarFactoryModel model;

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
				startButton.setActionCommand(CONTINUE);
				startButton.setText("Continue");

				// TODO: pause
			} else if (CONTINUE.equals(e.getActionCommand())) {
				startButton.setActionCommand(PAUSE);
				startButton.setText("Pause");

				// TODO: continue
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
			logger.debug("logSales checkbox action");
			model.toggleLoggingSales();
		});
	}
}
