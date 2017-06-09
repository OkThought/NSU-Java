package ru.nsu.ccfit.bogush.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AlertDialog extends JDialog {
	private static final Dimension SIZE = new Dimension(150, 80);

	public AlertDialog(Frame owner, String title, String description) {
		super(owner, title, true);
		Container contentPanel = this.getContentPane();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JLabel alertLabel = new JLabel(description);
		alertLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(alertLabel);

		JButton alertButton = new JButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		alertButton.setText("Ok");
		alertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(alertButton);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(SIZE);
		this.setResizable(false);
		this.setVisible(true);
	}
}
