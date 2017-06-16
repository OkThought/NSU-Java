package ru.nsu.ccfit.bogush.client.view;

import javax.swing.*;
import java.awt.*;

class ListElementRenderer extends DefaultListCellRenderer {
	private static final String FORMAT =
			"<html>" +
					"<body WIDTH=%d>" +
							"%s" +
					"</body>" +
			"</html>";
	private int width = super.getWidth();

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
	                                              int index, boolean isSelected, boolean cellHasFocus) {
		String text = String.format(FORMAT, width, value.toString());
		return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
	}

	void setWidth(int width) {
		this.width = width;
	}
}
