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
	private final int minValue;
	private final int maxValue;
	private int value;
	private String valueString;
	private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
	private boolean parseFailed = false;

	private static final String LOGGER_NAME = "LabeledSliderWithTextField";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public LabeledSliderWithTextField(String text, int minValue, int maxValue, int interval) {
		logger.traceEntry();
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
		logger.traceExit();
	}

	public void setValue(int value) {
		logger.traceEntry();
		logger.trace(this + ": set value to " + value);
		this.value = value;
		this.valueString = String.valueOf(value);
		slider.setValue(value);
		textField.setText(valueString);
		valueChanged(value);
		logger.traceExit();
	}

	public void setValue(String valueString) {
		logger.traceEntry();
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
		logger.traceExit();
	}

	private boolean parseValue(String valueString) {
		logger.traceEntry();
		boolean parsingCompleted = true;
		if (!isInteger(valueString)) {
			parsingCompleted = false;
			logger.error(valueString + " is not an Integer");
		} else {
			try {
				value = Integer.parseInt(valueString);
				value = Integer.max(minValue, value);
				value = Integer.min(maxValue, value);
			} catch (NumberFormatException e) {
				logger.error(e.getMessage() + ": couldn't parse value from '" + valueString + '\'');
				parsingCompleted = false;
			}
		}
		return logger.traceExit(parsingCompleted);
	}

	private static boolean isInteger(String str) {
		logger.traceEntry();
		boolean result = false;
		if (!str.isEmpty()) {
			boolean hasSign = str.charAt(0) == '-' || str.charAt(0) == '+';
			if (hasSign && str.length() == 1) {
				result = false;
			} else {
				result = true;
				for (int i = hasSign ? 1 : 0; i < str.length(); i++) {
					if (!Character.isDigit(str.charAt(i))) {
						result = false;
						break;
					}
				}
			}
		}
		return logger.traceExit(result);
	}

	private void valueChanged(int value) {
		logger.traceEntry();
		logger.trace(this + ": value changed to " + value);
		for (ValueChangeListener valueChangeListener : valueChangeListeners) {
			valueChangeListener.valueChanged(value);
		}
		logger.traceExit();
	}

	public void addValueChangeListener(ValueChangeListener valueChangeListener) {
		logger.traceEntry();
		valueChangeListeners.add(valueChangeListener);
		logger.traceExit();
	}

	public int getValue() {
		logger.traceEntry();
		return logger.traceExit(slider.getValue());
	}

	@Override
	public String toString() {
		return label.getText();
	}

	public interface ValueChangeListener { void valueChanged(int value); }
}
