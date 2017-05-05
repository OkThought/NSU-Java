package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class LabeledSliderWithTextField extends JComponent {
	private JLabel label;
	private JSlider slider;
	private JTextField textField;
	private JPanel panel;
	private int minValue;
	private int maxValue;
	private List<Listener> listeners = new ArrayList<>();
	private boolean failedToParse = false;

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

		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				logger.debug("slider action");
				int value = slider.getValue();
				syncTextField(value);
				valueChanged(value);
			}
		});

		textField.addActionListener(e -> {
			logger.debug("textField action");
			syncSlider(textField.getText());
		});
	}

	public void setTextAndValue(int value) {
		logger.debug("set text and value to " + value);
		setValue(value);
		setText(String.valueOf(value));
	}

	public void setTextAndValue(String valueString) {
		logger.debug("set text and value to " + valueString);
		setValue(valueString);
		setText(String.valueOf(valueString));
	}

	public void setValue(int value) {
		logger.debug("set slider's value to " + value);
		slider.setValue(value);
	}

	public void setValue(String valueString) {
		logger.debug("set slider's value to " + valueString);
		int value = parseValue(valueString);
		if (failedToParse) return;
		setValue(value);
	}

	public void setText(String text) {
		logger.debug("set text field's text to " + text);
		textField.setText(text);
	}

	public int getValue() {
		return slider.getValue();
	}

	public String getText() {
		return textField.getText();
	}

	private void syncSlider(String valueString) {
		logger.debug("sync slider with text field's text '" + valueString + '\'');
		setValue(valueString);
	}

	private void syncTextField(int value) {
		logger.debug("sync text field with slider's value " + value);
		setText(String.valueOf(value));
	}

	private int parseValue(String valueString) {
		failedToParse = false;
		int value;
		try {
			value = Integer.parseInt(valueString);
		} catch (NumberFormatException e) {
			logger.error(e.getMessage() + ": couldn't parse value from '" + valueString + '\'');
			failedToParse = true;
			return 0;
		}
		value = Integer.max(minValue, value);
		value = Integer.min(maxValue, value);
		return value;
	}

	private void valueChanged(int value) {
		logger.debug("value changed to " + value);
		for (Listener listener: listeners) {
			listener.valueChanged(value);
		}
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}


	public interface Listener {
		void valueChanged(int value);
	}
}