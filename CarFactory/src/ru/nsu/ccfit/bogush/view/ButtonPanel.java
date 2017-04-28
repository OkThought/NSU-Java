package ru.nsu.ccfit.bogush.view;

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
	private CarFactoryModel model;

	public ButtonPanel(CarFactoryModel model) {
		this.model = model;
		startButton.setActionCommand(START);
		startButton.addActionListener(e -> {
			if (START.equals(e.getActionCommand())) {
				startButton.setActionCommand(PAUSE);
				startButton.setText("Pause");

				model.getEngineSupplier().getThread().start();
				model.getBodySupplier().getThread().start();
				for (Supplier supplier: model.getAccessorySuppliers()) {
					supplier.getThread().start();
				}
				model.getStore().start();
			} else if (PAUSE.equals(e.getActionCommand())) {
				startButton.setActionCommand(CONTINUE);
				startButton.setText("Continue");

				// TODO: pause
			} else if (CONTINUE.equals(e.getActionCommand())) {
				startButton.setActionCommand(PAUSE);
				startButton.setText("Pause");

				// TODO: continue
			} else {
				System.err.println("Shouldn't get here");
				System.exit(1);
			}
		});

		resetButton.addActionListener(e -> {
			// TODO: reset
		});
	}
}
