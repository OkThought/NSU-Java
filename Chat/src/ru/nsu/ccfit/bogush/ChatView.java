package ru.nsu.ccfit.bogush;

import javax.swing.*;
import java.awt.*;

public class ChatView extends JFrame {
	private JPanel panel;

	public ChatView(String title) throws HeadlessException {
		super(title);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
