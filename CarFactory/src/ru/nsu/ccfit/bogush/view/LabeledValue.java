package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class LabeledValue extends JPanel {
	private JLabel valueLabel;
	private JLabel label;
	private JPanel panel;

	private int value;

	private static final Logger logger = LogManager.getLogger();

	public LabeledValue(String labelText) {
		logger.traceEntry();
		label.setText(labelText);
		logger.traceExit();
	}

	public void setEnabled(boolean enabled) {
		logger.traceEntry();
		valueLabel.setEnabled(enabled);
		logger.traceExit();
	}

	public void setValue(int value) {
		logger.traceEntry();
		this.value = value;
		valueLabel.setText(String.valueOf(value));
		logger.traceExit();
	}

	public int getValue() {
		logger.traceEntry();
		return logger.traceExit(value);
	}
}
