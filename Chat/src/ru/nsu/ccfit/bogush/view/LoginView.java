package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.LoginPayload;
import ru.nsu.ccfit.bogush.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;

public class LoginView extends JFrame {
	private static final Logger logger = LogManager.getLogger();

	private static final String TITLE = "Chat";
	private static final String LOGIN_BUTTON_TEXT = "User";
	private static final String NICK_LABEL_NAME = "Enter your nickname: ";

	private static final Dimension NICK_TEXT_FIELD_MIN_SIZE = new Dimension(50, 30);
	private static final Dimension NICK_TEXT_FIELD_PREF_SIZE = new Dimension(100, 30);
	private static final Dimension NICK_TEXT_FIELD_MAX_SIZE = new Dimension(100, 30);

	private static final Border ROOT_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

	private LoginHandler[] loginHandlers;

	private JPanel rootPanel;
	private JTextField nickTextField;

	public LoginView() throws HeadlessException {
		super(TITLE);
		createComponents();
		this.setContentPane(rootPanel);
		this.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void createComponents() {
		rootPanel = new JPanel();
		rootPanel.setBorder(ROOT_BORDER);
		rootPanel.setOpaque(true);

		nickTextField = new JTextField();
		nickTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					logger.trace("Enter pressed while typing");
					login();
				}
			}
		});
		nickTextField.setMinimumSize(NICK_TEXT_FIELD_MIN_SIZE);
		nickTextField.setPreferredSize(NICK_TEXT_FIELD_PREF_SIZE);
		nickTextField.setMaximumSize(NICK_TEXT_FIELD_MAX_SIZE);

		JLabel nickLabel = new JLabel(NICK_LABEL_NAME);

		JButton loginButton = new JButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.trace("User button clicked");
				login();
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

//	public void addLoginHandler(LoginHandler handler) {
//		loginHandlers.add(handler);
//	}


	public void setLoginHandlers(Collection<LoginHandler> loginHandlers) {
		this.loginHandlers = loginHandlers.toArray(new LoginHandler[loginHandlers.size()]);
	}

	private void alert(String title, String description) {
		new AlertDialog(this, title, description);
	}

	private void login() {
		String nickname = nickTextField.getText().trim();
		if (nickname.isEmpty()) {
			logger.trace("nickname is empty");
			alert("Login", "Nickname is empty!");
		} else {
			for (LoginHandler handler : loginHandlers) {
				handler.login(new LoginPayload(nickname));
			}
		}
	}
}
