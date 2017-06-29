package ru.nsu.ccfit.bogush.chat.client.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AlertDialog extends JDialog {
	private static final Dimension SIZE = new Dimension(150, 80);
	private static final int MARGIN = 10;
	private static final Border MARGIN_BORDER = BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);
	private static final String ALERT_LABEL_FORMAT_STRING =
			"<html>" +
					"<body WIDTH=%d style='text-align:center'>" +
							"%s" +
					"<body>" +
			"</html>";

	public AlertDialog(Frame owner, String title, String description) {
		super(owner, title, true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setMinimumSize(SIZE);
		this.setResizable(false);

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(MARGIN_BORDER);

		JLabel alertLabel = new JLabel(String.format(ALERT_LABEL_FORMAT_STRING, SIZE.width-MARGIN*2, description));
		alertLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(alertLabel, BorderLayout.CENTER);

		JButton alertButton = new JButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		alertButton.setText("Ok");
		alertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(alertButton, BorderLayout.SOUTH);

		this.setContentPane(contentPanel);
		this.pack();
		this.setVisible(true);
	}
}
