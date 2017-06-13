package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.UserListChangeListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class ChatView extends JFrame {
	private static final Logger logger = LogManager.getLogger(ChatView.class.getSimpleName());
	private static final String TITLE = "Chat";
	private static final Color MESSAGE_BACKGROUND_COLOR = new Color(150, 150, 250, 100);
	private static final int MARGIN = 4;
	private static final Dimension MESSAGES_PANEL_SIZE = new Dimension(-1, 250);
	private static final Dimension COMPOSE_PANEL_SIZE = new Dimension(-1, 30);

	private ViewController viewController;

	private HashMap<User, JComponent> userComponentMap = new HashMap<>();

	private JSplitPane root;
	private JPanel messagesPanel;
	private JPanel userListPanel;

	public ChatView(ViewController viewController) throws HeadlessException {
		super(TITLE);
		this.viewController = viewController;
		createComponents();
		this.setContentPane(root);
		this.setLocationRelativeTo(null);
		this.setMinimumSize(new Dimension(400, 200));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	void addUser(User user) {
		logger.trace("Add {}", user);
		if (userComponentMap.containsKey(user)) {
			logger.warn("User already in the component map");
		} else {
			JComponent userComponent = createUserComponent(user);
			userComponentMap.put(user, userComponent);
			userListPanel.add(userComponent);
		}
		userListPanel.updateUI();
	}

	void removeUser(User user) {
		logger.trace("Remove {}", user);
		if (userComponentMap.containsKey(user)) {
			userListPanel.remove(userComponentMap.get(user));
			userComponentMap.remove(user);
		} else {
			logger.error("User is absent in the component map");
		}
		userListPanel.updateUI();
	}

	void removeAllUsers() {
		userComponentMap.clear();
		userListPanel.removeAll();
		userListPanel.updateUI();
	}

	private void createComponents() {
		userListPanel = createUserListPanel();
		JSplitPane chatPanel = createChatPane();

		root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userListPanel, chatPanel);
		root.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
		root.setOpaque(true);
	}

	private JPanel createUserListPanel() {
		JPanel userListPanel = new JPanel();
		userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
		Border line = BorderFactory.createLineBorder(Color.BLACK);
		Border margin = BorderFactory.createEmptyBorder(MARGIN,MARGIN,MARGIN,MARGIN);
		userListPanel.setBorder(BorderFactory.createCompoundBorder(line, margin));
		userListPanel.setMinimumSize(new Dimension(100, -1));
		return userListPanel;
	}

	private JComponent createUserComponent(User user) {
		JLabel label = new JLabel(user.getNickname());
		label.setHorizontalAlignment(SwingConstants.LEFT);
		return label;
	}

	private JSplitPane createChatPane() {
		JSplitPane chatPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		messagesPanel = new JPanel();
		messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
		messagesPanel.setBackground(Color.WHITE);
		messagesPanel.setMinimumSize(MESSAGES_PANEL_SIZE);

		JScrollPane chatScrollPane = new JScrollPane(messagesPanel);
		chatScrollPane.setLayout(new ScrollPaneLayout());
		chatScrollPane.setBorder(BorderFactory.createEmptyBorder(MARGIN,MARGIN,MARGIN,MARGIN));

		chatPane.setTopComponent(chatScrollPane);

		JPanel composePanel = createComposePanel();
		chatPane.setBottomComponent(composePanel);
		chatPane.setResizeWeight(BOTTOM_ALIGNMENT);
		return chatPane;
	}

	private JPanel createComposePanel() {
		JPanel composePanel = new JPanel();
		composePanel.setLayout(new BorderLayout());


		JPanel buttonContainer = new JPanel();
		JTextArea composeTextArea = new JTextArea();
		JButton sendButton = new JButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.trace("Send button was clicked");
				sendMessage(composeTextArea.getText());
				composeTextArea.setText("");
			}
		});
		sendButton.setText("Send");
		buttonContainer.add(sendButton);
		composePanel.add(buttonContainer, BorderLayout.LINE_END);
		composePanel.setMinimumSize(COMPOSE_PANEL_SIZE);

		composeTextArea.setBorder(BorderFactory.createEmptyBorder(MARGIN,MARGIN,MARGIN,MARGIN));
		composeTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (!e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					sendButton.doClick();
				}
			}
		});
		JScrollPane composeScrollPane = new JScrollPane(composeTextArea);
		composePanel.add(composeScrollPane, BorderLayout.CENTER);
		return composePanel;
	}

	private void sendMessage(String msg) {
		messagesPanel.add(createMessageComponent(msg));
		messagesPanel.updateUI();
		viewController.sendTextMessage(msg);
	}

	private JComponent createMessageComponent(String msg) {
		JLabel messageLabel = new JLabel(msg);
		messageLabel.setOpaque(true);
		messageLabel.setBackground(MESSAGE_BACKGROUND_COLOR);
		return messageLabel;
	}
}
