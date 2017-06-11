package ru.nsu.ccfit.bogush.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AlertDialog extends JDialog {
	private static final Dimension SIZE = new Dimension(150, 80);
	private static final Border MARGIN_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

	public AlertDialog(Frame owner, String title, String description) {
		super(owner, title, true);
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(MARGIN_BORDER);

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

		this.setContentPane(contentPanel);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(SIZE);
		this.setResizable(false);
		this.setVisible(true);
	}
}
