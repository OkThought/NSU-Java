package ru.nsu.ccfit.bogush.view;

import javax.swing.*;

public class LabeledValue extends JPanel {
	private JLabel valueLabel;
	private JLabel label;
	private JPanel panel;

	private int value;

	public LabeledValue(String labelText) {
		label.setText(labelText);
	}

	public void setEnabled(boolean enabled) {
		valueLabel.setEnabled(enabled);
	}

	public void setValue(int value) {
		this.value = value;
		valueLabel.setText(String.valueOf(value));
	}

	public int getValue() {
		return value;
	}
}
