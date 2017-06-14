package ru.nsu.ccfit.bogush.client.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.types.TextMessage;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class ChatView extends JFrame {
	private static final Logger logger = LogManager.getLogger(ChatView.class.getSimpleName());
	private static final String TITLE = "Chat";
	private static final int MARGIN = 4;
	private static final Border MARGIN_BORDER = BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);
	private static final Dimension COMPOSE_PANEL_SIZE = new Dimension(-1, 30);
	private static final Dimension USER_LIST_PANE_SIZE = new Dimension(100, -1);

	private ViewController viewController;

	private JTextArea composeTextArea;
	private JSplitPane chatPane;
	private JSplitPane root;
	private JScrollPane userListPane;
	private DefaultListModel<User> userListModel;
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
		if (userListModel.contains(user)) {
			logger.warn("User already in the list");
		} else {
			userListModel.addElement(user);
		}
		userListPane.validate();
		userListPane.repaint();
	}

	void removeUser(User user) {
		logger.trace("Removing {}", user);
		if (userListModel.contains(user)) {
			userListModel.removeElement(user);
		} else {
			logger.error("User is absent in the list");
		}
		userListPane.validate();
		userListPane.repaint();
	}

	void removeAllUsers() {
		userListModel.removeAllElements();
		userListPane.validate();
		userListPane.repaint();
	}

	private void createComponents() {
		createUserListPane();
		createChatPane();
		root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userListPane, chatPane);
		root.setBorder(MARGIN_BORDER);
		root.setOpaque(true);
	}

	private void createUserListPane() {
		userListModel = new DefaultListModel<>();
		JList<User> userList = new JList<>(userListModel);
		userListPane = new JScrollPane(userList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		userListPane.setBorder(MARGIN_BORDER);
		userListPane.setMinimumSize(USER_LIST_PANE_SIZE);
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
