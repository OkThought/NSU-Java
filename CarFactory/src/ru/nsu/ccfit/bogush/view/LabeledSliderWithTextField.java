package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class LabeledSliderWithTextField extends JComponent {
	private JLabel label;
	private JSlider slider;
	private JTextField textField;
	private JPanel panel;
	private int minValue;
	private int maxValue;
	private int value;
	private String valueString;
	private List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
	private boolean parseFailed = false;

	private static final String LOGGER_NAME = "LabeledSliderWithTextField";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public LabeledSliderWithTextField(String text, int minValue, int maxValue, int interval) {
		label.setText(text);
		this.minValue = minValue;
		this.maxValue = maxValue;
		slider.setMinimum(minValue);
		slider.setMaximum(maxValue);
		slider.setMajorTickSpacing(interval);
		slider.setMinorTickSpacing(interval);

		slider.addChangeListener(e -> textField.setText(String.valueOf(slider.getValue())));

		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				logger.trace("slider action");
				setValue(slider.getValue());
			}
		});

		textField.addActionListener(e -> {
			logger.trace("textField action");
			setValue(textField.getText().trim());
		});

		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String text = (textField.getText() + e.getKeyChar()).trim();
				logger.trace("key typed, current string: " + text);
				textField.setForeground(isInteger(text) ? Color.BLACK:Color.RED);
			}
		});
	}

	public void setValue(int value) {
		logger.trace(this + ": set value to " + value);
		this.value = value;
		this.valueString = String.valueOf(value);
		slider.setValue(value);
		textField.setText(valueString);
		valueChanged(value);
	}

	public void setValue(String valueString) {
		logger.trace(this + ": set value to " + valueString);
		valueString = valueString.trim();
		int prevValue = value;
		if (parseValue(valueString)) {
			this.valueString = valueString;
			slider.setValue(value);
			textField.setText(valueString);
			valueChanged(value);
		} else {
			value = prevValue;
		}
	}

	private boolean parseValue(String valueString) {
		if (!isInteger(valueString)) {
			logger.error(valueString + " is not an Integer");
			return false;
		}
		try {
			value = Integer.parseInt(valueString);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage() + ": couldn't parse value from '" + valueString + '\'');
			return false;
		}
		value = Integer.max(minValue, value);
		value = Integer.min(maxValue, value);
		return true;
	}

	private static boolean isInteger(String str) {
		if (str.isEmpty()) return false;
		boolean hasSign = str.charAt(0) == '-' || str.charAt(0) == '+';
		if (hasSign && str.length() == 1) return false;
		for (int i = hasSign ? 1:0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) return false;
		}
		return true;
	}

	private void valueChanged(int value) {
		logger.trace(this + ": value changed to " + value);
		for (ValueChangeListener valueChangeListener : valueChangeListeners) {
			valueChangeListener.valueChanged(value);
		}
	}

	public void addValueChangeListener(ValueChangeListener valueChangeListener) {
		valueChangeListeners.add(valueChangeListener);
	}

	@Override
	public String toString() {
		return label.getText();
	}

	public int getValue() {
		return slider.getValue();
	}

	public interface ValueChangeListener { void valueChanged(int value); }
}
