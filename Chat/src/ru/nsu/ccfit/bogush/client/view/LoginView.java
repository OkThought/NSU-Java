package ru.nsu.ccfit.bogush.client.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame {
	private static final Logger logger = LogManager.getLogger(LoginView.class.getSimpleName());

	private static final String TITLE = "Login";
	private static final String LOGIN_BUTTON_TEXT = "Login";
	private static final String NICK_LABEL_NAME = "Enter your nickname: ";

	private static final Dimension NICK_TEXT_FIELD_MIN_SIZE = new Dimension(50, 30);
	private static final Dimension NICK_TEXT_FIELD_PREF_SIZE = new Dimension(100, 30);
	private static final Dimension NICK_TEXT_FIELD_MAX_SIZE = new Dimension(100, 30);

	private static final Border ROOT_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

	private ViewController viewController;

	private JPanel rootPanel;
	private JTextField nickTextField;

	public LoginView(ViewController viewController) throws HeadlessException {
		super(TITLE);
		this.viewController = viewController;
		createComponents();
		this.setContentPane(rootPanel);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void createComponents() {
		rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		rootPanel.setBorder(ROOT_BORDER);
		rootPanel.setOpaque(true);

		nickTextField = new JTextField();
		Action loginAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.trace("Login action performed");
				login();
			}
		};

		nickTextField.addActionListener(loginAction);
		nickTextField.setMinimumSize(NICK_TEXT_FIELD_MIN_SIZE);
		nickTextField.setPreferredSize(NICK_TEXT_FIELD_PREF_SIZE);
		nickTextField.setMaximumSize(NICK_TEXT_FIELD_MAX_SIZE);

		JLabel nickLabel = new JLabel(NICK_LABEL_NAME);

		JButton loginButton = new JButton(loginAction);
		loginButton.setText(LOGIN_BUTTON_TEXT);

		nickLabel.setAlignmentX(CENTER_ALIGNMENT);
		nickTextField.setAlignmentX(CENTER_ALIGNMENT);
		loginButton.setAlignmentX(CENTER_ALIGNMENT);

		rootPanel.add(nickLabel);
		rootPanel.add(nickTextField);
		rootPanel.add(loginButton);
	}

	private void alert(String title, String description) {
		new AlertDialog(this, title, description);
	}

	private void login() {
		String nickname = nickTextField.getText().trim();
		if (nickname.isEmpty()) {
			logger.trace("Nickname is empty");
			alert(TITLE, "Nickname is empty!");
		} else {
			viewController.login(nickname);
		}
	}
}
