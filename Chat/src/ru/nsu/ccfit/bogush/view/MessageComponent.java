package ru.nsu.ccfit.bogush.view;

import javax.swing.*;
import java.awt.*;

public class MessageComponent extends JPanel {
	private static final int HORIZONTAL_GAP = 10;
	private static final int VERTICAL_GAP = 5;
	private static final Color BACKGROUND = new Color(98, 94, 255);
	private TextMessage message;
	private LayoutManager layout;
	private JLabel author;
	private JTextArea text;

	public MessageComponent(TextMessage message) {
		super();
		this.message = message;
		createComponents();
		setLayout(layout);
		add(author);
		add(text);
		setOpaque(true);
		setBackground(BACKGROUND);
	}

	private void createComponents() {
		layout = new FlowLayout(FlowLayout.LEADING, HORIZONTAL_GAP, VERTICAL_GAP);
		author = new JLabel(message.getAuthor().getNickname());
		text = new JTextArea(message.getText());
	}
}