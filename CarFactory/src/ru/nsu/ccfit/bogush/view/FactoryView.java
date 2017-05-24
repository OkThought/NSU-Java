package ru.nsu.ccfit.bogush.view;

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

	private CarFactoryModel model;

	public FactoryView(CarFactoryModel model) {
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
				model.getCarFactory().getThreadPool().stop();
			}
		});
		frame.setResizable(false);
		frame.setLocation(0, 0);
		frame.pack();
		frame.setVisible(true);
	}

	private void createUIComponents() {
		controlPanel = new ControlPanel(model);
		infoPanel = new InformationPanel(model);
		buttonPanel = new ButtonPanel(model);
	}
}
