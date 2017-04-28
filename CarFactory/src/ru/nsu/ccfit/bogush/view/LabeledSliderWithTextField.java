package ru.nsu.ccfit.bogush.view;

import javax.swing.*;
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

	public LabeledSliderWithTextField(String text, int minValue, int maxValue, int interval) {
//		super();
		label.setText(text);
		this.minValue = minValue;
		this.maxValue = maxValue;
		slider.setMinimum(minValue);
		slider.setMaximum(maxValue);
		slider.setMajorTickSpacing(interval);
		slider.setMinorTickSpacing(interval);

		slider.addChangeListener(e -> {
			setValue(slider.getValue());
			inputPerformed();
		});

		textField.addActionListener(e -> {
			setText(textField.getText());
			inputPerformed();
		});
	}

	public void setText(String textValue) {
		int value;
		try {
			value = Integer.parseInt(textValue);
		} catch (NumberFormatException e) {
			return;
		}
		value = Integer.max(minValue, value);
		value = Integer.min(maxValue, value);
		setValue(value);
	}

	public int getValue() {
		return slider.getValue();
	}

	public String getText() {
		return textField.getText();
	}

	public void setValue(int value) {
		slider.setValue(value);
		textField.setText(String.valueOf(value));
	}

	private void inputPerformed() {
		for (Listener listener: listeners) {
			listener.inputPerformed(slider.getValue());
		}
	}

	public void addListener(Listener listener) {
		listeners.add(listener);
	}


	public interface Listener {
		void inputPerformed(int value);
	}
}
