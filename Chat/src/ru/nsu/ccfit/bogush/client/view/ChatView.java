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
	private static final Dimension COMPOSE_PANEL_SIZE = new Dimension(100, 30);
	private static final Dimension USER_LIST_PANE_SIZE = new Dimension(100, -1);

	private ViewController viewController;

	private JTextArea composeTextArea;
	private JSplitPane chatPane;
	private JSplitPane root;

	private JScrollPane userListScrollPane;
	private JScrollPane chatScrollPane;

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
		userListScrollPane.validate();
		userListScrollPane.repaint();
	}

	void removeUser(User user) {
		logger.trace("Removing {}", user);
		if (userListModel.contains(user)) {
			userListModel.removeElement(user);
		} else {
			logger.error("User is absent in the list");
		}
		userListScrollPane.validate();
		userListScrollPane.repaint();
	}

	void removeAllUsers() {
		userListModel.removeAllElements();
		userListScrollPane.validate();
		userListScrollPane.repaint();
	}

	private void createComponents() {
		createUserListPane();
		createChatPane();
		root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, userListScrollPane, chatPane);
		root.setBorder(MARGIN_BORDER);
		root.setOpaque(true);
	}

	private void createUserListPane() {
		userListModel = new DefaultListModel<>();
		JList<User> userList = new JList<>(userListModel);
		ListElementRenderer renderer = new ListElementRenderer();
		userList.setCellRenderer(renderer);
		userListScrollPane = new JScrollPane(userList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		userListScrollPane.setBorder(MARGIN_BORDER);
		userListScrollPane.setMinimumSize(USER_LIST_PANE_SIZE);

		JViewport viewport = userListScrollPane.getViewport();
		viewport.addChangeListener(e -> {
			renderer.setWidth(viewport.getWidth());
			userListScrollPane.validate();
			userListScrollPane.repaint();
		});
	}

	private void createChatPane() {
		chatPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		messageListModel = new DefaultListModel<>();
		JList<String> messageList = new JList<>(messageListModel);
		ListElementRenderer renderer = new ListElementRenderer();
		messageList.setCellRenderer(renderer);

		chatScrollPane = new JScrollPane(messageList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setLayout(new ScrollPaneLayout());
		chatScrollPane.setBorder(MARGIN_BORDER);

		JViewport viewport = chatScrollPane.getViewport();
		viewport.addChangeListener(e -> {
			renderer.setWidth(viewport.getWidth());
			chatScrollPane.validate();
			chatScrollPane.repaint();
		});

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
		appendMessage(viewController.getUser().getNickname(), text);
		viewController.sendTextMessage(text);
	}

	void appendMessage(String author, String text) {
		messageListModel.addElement(author + ": " + text);
		chatScrollPane.validate();
		chatScrollPane.repaint();
	}
}
