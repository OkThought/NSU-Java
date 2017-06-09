package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatView extends JFrame {
	private static final Logger logger = LogManager.getLogger();
	private static final String TITLE = "Chat";
	private static final Color MESSAGE_BACKGROUND_COLOR = new Color(150, 150, 250, 100);
	private static final int MARGIN = 4;
	private static final Dimension MESSAGES_PANEL_SIZE = new Dimension(-1, 250);
	private static final Dimension COMPOSE_PANEL_SIZE = new Dimension(-1, 30);

	private JSplitPane root;
	private JPanel messagesPanel;

	public ChatView() throws HeadlessException {
		super(TITLE);
		createComponents();
		this.setContentPane(root);
		this.setLocationRelativeTo(null);
		this.setMinimumSize(new Dimension(400, 200));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void createComponents() {
		JPanel userListPanel = createUserListPanel();
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

		JTextArea composeTextArea = new JTextArea();
		composeTextArea.setBorder(BorderFactory.createEmptyBorder(MARGIN,MARGIN,MARGIN,MARGIN));
		composeTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage(composeTextArea.getText());
					composeTextArea.setText("");
				}
			}
		});
		JScrollPane composeScrollPane = new JScrollPane(composeTextArea);

		composePanel.add(composeScrollPane, BorderLayout.CENTER);

		JPanel buttonContainer = new JPanel();
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
		return composePanel;
	}

	private void sendMessage(String msg) {
		messagesPanel.add(createMessage(msg));
		messagesPanel.updateUI();
	}

	private JComponent createMessage(String msg) {
		JLabel messageLabel = new JLabel(msg);
		messageLabel.setOpaque(true);
		messageLabel.setBackground(MESSAGE_BACKGROUND_COLOR);
		return messageLabel;
	}
}
