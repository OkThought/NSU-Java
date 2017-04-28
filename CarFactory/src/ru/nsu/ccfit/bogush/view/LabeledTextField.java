package ru.nsu.ccfit.bogush.view;

import javax.swing.*;

public class LabeledTextField extends JComponent {
	private JTextField textField;
	private JLabel label;
	private JPanel panel;

	public LabeledTextField(String labelText, boolean enabled) {
		label.setText(labelText);
		textField.setEnabled(enabled);
		label.addPropertyChangeListener(evt -> {
			if ("label".equals(evt.getPropertyName())) {
				label.setText((String) evt.getNewValue());
			}
		});
	}

	public void setEnabled(boolean enabled) {
		textField.setEnabled(enabled);
	}

	public void setValue(int value) {
		setText(String.valueOf(value));
	}

	public void setText(String text) {
		textField.setText(text);
	}

	public String getText() {
		return textField.getText();
	}
}
