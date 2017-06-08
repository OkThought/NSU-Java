package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class LoginView extends JFrame {
	private static final Logger logger = LogManager.getLogger();
	private static final String TITLE = "Chat";
	private static final String LOGIN_BUTTON_TEXT = "Login";
	private static final String NICKNAME_LABEL_NAME = "Enter your nickname: ";

	private ArrayList<LoginHandler> loginHandlers = new ArrayList<>();

	private JPanel rootPanel;
	private JTextField nickTextField;

	public LoginView() throws HeadlessException {
		super(TITLE);
		createComponents();
		this.setContentPane(rootPanel);
		this.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		this.pack();
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void createComponents() {
		rootPanel = new JPanel();
		rootPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		rootPanel.setOpaque(true);

		nickTextField = new JTextField();
		nickTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					logger.trace("Enter pressed while typing");
					login(nickTextField.getText());
				}
			}
		});
		nickTextField.setMinimumSize(new Dimension(50, 30));
		nickTextField.setPreferredSize(new Dimension(100, 30));
		nickTextField.setMaximumSize(new Dimension(100, 30));

		JLabel nickLabel = new JLabel(NICKNAME_LABEL_NAME);

		JButton loginButton = new JButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.trace("Login button clicked");
				login(nickTextField.getText());
			}
		});
		loginButton.setText(LOGIN_BUTTON_TEXT);

		nickLabel.setAlignmentX(CENTER_ALIGNMENT);
		nickTextField.setAlignmentX(CENTER_ALIGNMENT);
		loginButton.setAlignmentX(CENTER_ALIGNMENT);

		rootPanel.add(nickLabel);
		rootPanel.add(nickTextField);
		rootPanel.add(loginButton);
	}

	public void addLoginHandler(LoginHandler handler) {
		loginHandlers.add(handler);
	}

	private void login(String message) {
		for (LoginHandler handler : loginHandlers) {
			handler.login(message);
		}
	}

	public interface LoginHandler {
		void login(String message);
	}
}
