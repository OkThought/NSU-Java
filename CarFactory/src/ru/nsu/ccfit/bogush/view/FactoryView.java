package ru.nsu.ccfit.bogush.view;

import ru.nsu.ccfit.bogush.CarFactoryModel;

import javax.swing.*;

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
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}

	private void createUIComponents() {
		controlPanel = new ControlPanel(model);
		infoPanel = new InformationPanel(model);
		buttonPanel = new ButtonPanel(model);
	}
}
