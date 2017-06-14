package ru.nsu.ccfit.bogush.client.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

class ChatView extends JFrame {
	private static final Logger logger = LogManager.getLogger(ChatView.class.getSimpleName());
	private static final String TITLE = "Chat";
	private static final int MARGIN = 4;
	private static final Border MARGIN_BORDER = BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);
	private static final Dimension COMPOSE_PANEL_SIZE = new Dimension(-1, 30);
	private static final Dimension USER_LIST_PANEL_SIZE = new Dimension(100, -1);

	private ViewController viewController;

	private HashMap<User, JComponent> userComponentMap = new HashMap<>();

	private JTextArea composeTextArea;
	private JSplitPane chatPane;
	private JSplitPane root;
	private JPanel userListPanel;
	private DefaultListModel<String> messageListModel;

	ChatView(ViewController viewController) throws HeadlessException {
		super(TITLE);
		this.viewController = viewController;
		createComponents();
		this.setContentPane(root);
		this.setLocationRelativeTo(null);
		this.setMinimumSize(new Dimension(400, 200));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	void addUser(User user) {
		logger.trace("Adding {}", user);
		if (userComponentMap.containsKey(user)) {
			logger.warn("User already in the component map");
		} else {
			JComponent userComponent = createUserComponent(user);
			userComponentMap.put(user, userComponent);
			userListPanel.add(userComponent);
		}
		userListPanel.validate();
		userListPanel.repaint();
	}

	void removeUser(User user) {
		logger.trace("Removing {}", user);
		if (userComponentMap.containsKey(user)) {
			userListPanel.remove(userComponentMap.get(user));
			userComponentMap.remove(user);
		} else {
			logger.error("User is absent in the component map");
		}
		userListPanel.validate();
		userListPanel.repaint();
	}

	void removeAllUsers() {
		userComponentMap.clear();
		userListPanel.removeAll();
		userListPanel.validate();
		userListPanel.repaint();
	}

	private void createComponents() {
		userListPanel = createUserListPanel();
		createChatPane();
		root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userListPanel, chatPane);
		root.setBorder(MARGIN_BORDER);
		root.setOpaque(true);
	}

	private JPanel createUserListPanel() {
		JPanel userListPanel = new JPanel();
		userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
		Border line = BorderFactory.createLineBorder(Color.BLACK);
		userListPanel.setBorder(BorderFactory.createCompoundBorder(line, MARGIN_BORDER));
		userListPanel.setMinimumSize(USER_LIST_PANEL_SIZE);
		return userListPanel;
	}

	private JComponent createUserComponent(User user) {
		JLabel label = new JLabel(user.getNickname());
		label.setHorizontalAlignment(SwingConstants.LEFT);
		return label;
	}

	private void createChatPane() {
		chatPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		messageListModel = new DefaultListModel<>();
		JList<String> messageList = new JList<>(messageListModel);

		JScrollPane chatScrollPane = new JScrollPane(messageList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setLayout(new ScrollPaneLayout());
		chatScrollPane.setBorder(MARGIN_BORDER);

		JPanel composePanel = createComposePanel();

		chatPane.setTopComponent(chatScrollPane);
		chatPane.setBottomComponent(composePanel);
		chatPane.setResizeWeight(BOTTOM_ALIGNMENT);
	}

	private JPanel createComposePanel() {
		JPanel composePanel = new JPanel();
		composePanel.setLayout(new BorderLayout());


		JPanel buttonContainer = new JPanel();
		composeTextArea = new JTextArea();
		JButton sendButton = new JButton(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.trace("Send button was clicked");
				sendMessage();
			}
		});
		sendButton.setText("Send");
		buttonContainer.add(sendButton);
		composePanel.add(buttonContainer, BorderLayout.LINE_END);
		composePanel.setMinimumSize(COMPOSE_PANEL_SIZE);

		composeTextArea.setBorder(MARGIN_BORDER);
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

	private void sendMessage() {
		String text = composeTextArea.getText();
		composeTextArea.setText("");
		TextMessage msg = new TextMessage(viewController.getUser(), text);
		appendMessage(msg);
		viewController.sendTextMessage(msg);
	}

	void appendMessage(TextMessage msg) {
		messageListModel.addElement(msg.getAuthor().getNickname() + ": " + msg.getText());
		chatPane.validate();
		chatPane.repaint();
	}
}
