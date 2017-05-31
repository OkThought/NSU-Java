package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.CarFactoryModel;
import ru.nsu.ccfit.bogush.factory.Supplier;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FactoryView extends JPanel {
	private JPanel mainPanel;
	private ControlPanel controlPanel;
	private InformationPanel infoPanel;
	private ButtonPanel buttonPanel;
	private final CarFactoryModel model;

	private static final String LOGGER_NAME = "FactoryView";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public FactoryView(CarFactoryModel model) {
		logger.traceEntry();
		this.model = model;
		JFrame frame = new JFrame("Car Factory");
		frame.setContentPane(mainPanel);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				model.getStore().stop();
				model.getEngineSupplier().getThread().interrupt();
				model.getBodySupplier().getThread().interrupt();
				for (Supplier s : model.getAccessorySuppliers()) {
					s.getThread().interrupt();
				}
				model.getCarStorageController().stop();
				model.getCarFactory().getThreadPool().stop();
			}
		});
		frame.setResizable(false);
		frame.setLocation(0, 0);
		frame.pack();
		frame.setVisible(true);
		logger.traceExit();
	}

	private void createUIComponents() {
		logger.traceEntry();
		controlPanel = new ControlPanel(model);
		infoPanel = new InformationPanel(model);
		buttonPanel = new ButtonPanel(model, this);
		logger.traceExit();
	}

	public ControlPanel getControlPanel() {
		logger.traceEntry();
		return logger.traceExit(controlPanel);
	}

	public InformationPanel getInfoPanel() {
		logger.traceEntry();
		return logger.traceExit(infoPanel);
	}

	public ButtonPanel getButtonPanel() {
		logger.traceEntry();
		return logger.traceExit(buttonPanel);
	}
}
