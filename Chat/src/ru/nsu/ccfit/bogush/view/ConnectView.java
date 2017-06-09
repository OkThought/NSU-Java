package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class ConnectView extends JFrame {
	private static final Logger logger = LogManager.getLogger();

	private static final String TITLE = "Connect to Server";

	private JPanel rootPanel;

	public ConnectView() throws HeadlessException {
		super(TITLE);
		createComponents();
		this.setContentPane(rootPanel);
		this.setLayout(new SpringLayout());
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void createComponents() {
		rootPanel = new JPanel();
	}
}
