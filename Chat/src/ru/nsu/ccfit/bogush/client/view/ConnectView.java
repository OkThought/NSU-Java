package ru.nsu.ccfit.bogush.client.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ConnectView extends JFrame {
	private static final Logger logger = LogManager.getLogger(ConnectView.class.getSimpleName());

	private static final String TITLE = "Connect to Server";
	private static final String SERVER_IP_LABEL_TEXT = "Server ip:";

	private static final Dimension MIN_SIZE = new Dimension(250, 130);
	private static final Dimension MAX_SIZE = new Dimension(300, 200);

	private static final Dimension MARGIN = new Dimension(20, 20);
	private static final Dimension GAP = new Dimension(20, 30);
	private static final String SERVER_PORT_LABEL_TEXT = "Server port:";
	private static final Dimension TEXT_FIELD_SIZE = new Dimension(120, 24);
	private static final Dimension TEXT_FIELD_MAX_SIZE = new Dimension(150, 24);

	private ViewController viewController;

	private JTextField serverPortTextField;
	private JTextField serverIpTextField;
	private JPanel rootPanel;
	private SpringLayout layout;

	public ConnectView(ViewController viewController) throws HeadlessException {
		super(TITLE);
		this.viewController = viewController;
		createComponents();
		this.setContentPane(rootPanel);
		this.setMinimumSize(MIN_SIZE);
		this.setMaximumSize(MAX_SIZE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void horizontalConstraintBetween(Component left, Component right) {
		layout.putConstraint(SpringLayout.WEST, right, GAP.width, SpringLayout.EAST, left);
	}

	private void verticalConstraintBetween(Component up, Component down) {
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, down, GAP.height, SpringLayout.VERTICAL_CENTER, up);
	}

	private void westMarginConstraint(Component c) {
		layout.putConstraint(SpringLayout.WEST, c, MARGIN.width, SpringLayout.WEST, rootPanel);
	}

	private void eastMarginConstraint(Component c) {
		layout.putConstraint(SpringLayout.EAST, c, -MARGIN.width, SpringLayout.EAST, rootPanel);
	}

	private void northMarginConstraint(Component c) {
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, c, MARGIN.height, SpringLayout.NORTH, rootPanel);
	}

	private void southMarginConstraint(Component c) {
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, c, -MARGIN.height, SpringLayout.SOUTH, rootPanel);
	}

	private void createComponents() {
		rootPanel = new JPanel();
		layout = new SpringLayout();
		rootPanel.setLayout(layout);

		JLabel serverIpLabel = new JLabel(SERVER_IP_LABEL_TEXT);
		westMarginConstraint(serverIpLabel);
		northMarginConstraint(serverIpLabel);
		rootPanel.add(serverIpLabel);

		serverIpTextField = new JTextField();
		serverIpLabel.setLabelFor(serverIpTextField);
		serverIpTextField.setMaximumSize(TEXT_FIELD_MAX_SIZE);
		serverIpTextField.setPreferredSize(TEXT_FIELD_SIZE);
		northMarginConstraint(serverIpTextField);
		eastMarginConstraint(serverIpTextField);
		serverIpTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					serverIpTextField.transferFocus();
				}
			}
		});
		rootPanel.add(serverIpTextField);

		JLabel serverPortLabel = new JLabel(SERVER_PORT_LABEL_TEXT);
		verticalConstraintBetween(serverIpLabel, serverPortLabel);
		westMarginConstraint(serverPortLabel);
		rootPanel.add(serverPortLabel);

		serverPortTextField = new JTextField();
		verticalConstraintBetween(serverIpTextField, serverPortTextField);
		serverPortTextField.setMaximumSize(TEXT_FIELD_MAX_SIZE);
		serverPortTextField.setPreferredSize(TEXT_FIELD_SIZE);
		eastMarginConstraint(serverPortTextField);
		rootPanel.add(serverPortTextField);
		serverPortTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					logger.trace("Enter was pressed. Try to connect to server");
					connect();
				}
			}
		});

		JButton connectButton = new JButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.trace("Connect button clicked");
				connect();
			}
		});

		connectButton.setText("Connect");
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, connectButton, 0, SpringLayout.HORIZONTAL_CENTER, rootPanel);
		southMarginConstraint(connectButton);
		rootPanel.add(connectButton);
	}

	private void connect() {
		String host = serverIpTextField.getText().trim();
		String port = serverPortTextField.getText().trim();
		if (host.isEmpty()) {
			logger.trace("Host text field is empty");
			new AlertDialog(this, "Connect", "Host text field is empty");
		} else if (port.isEmpty()) {
			logger.trace("Port text field is empty");
			new AlertDialog(this, "Connect", "Port text field is empty");
		} else {
			viewController.connect(host, Integer.parseInt(port));
		}
	}
}
