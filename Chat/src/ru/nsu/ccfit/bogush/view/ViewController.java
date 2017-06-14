package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.Client;
import ru.nsu.ccfit.bogush.LoginPayload;
import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.UserListChangeListener;
import ru.nsu.ccfit.bogush.message.types.Text;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ViewController implements UserListChangeListener, ReceiveTextMessageHandler {
	private static final Logger logger = LogManager.getLogger(ViewController.class.getSimpleName());

	private Client client;

	private ConnectView connectView;
	private LoginView loginView;
	private ChatView chatView;

	private ArrayList<ConnectHandler> connectHandlers = new ArrayList<>();
	private ArrayList<DisconnectHandler> disconnectHandlers = new ArrayList<>();
	private ArrayList<LoginHandler> loginHandlers = new ArrayList<>();
	private ArrayList<LogoutHandler> logoutHandlers = new ArrayList<>();
	private ArrayList<SendTextMessageHandler> sendTextMessageHandlers = new ArrayList<>();

	public ViewController(Client client) {
		this.client = client;
		showConnectView();
	}

	private void createConnectView() {
		logger.trace("create connect window");
		connectView = new ConnectView(this);
		connectView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				logger.trace("connect window closed");
				loginView.dispose();
				disconnect();
			}
		});
	}

	private void createLoginView() {
		logger.trace("create login window");
		loginView = new LoginView(this);

		loginView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				logger.trace("login window closed");
				showConnectView();
			}
		});
	}

	private void createChatView() {
		logger.trace("create chat window");
		chatView = new ChatView(this);
		chatView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				logger.trace("chat window closed");
				logout();
			}
		});
	}

	private void showConnectView() {
		logger.trace("Show connect window");
		if (connectView == null) createConnectView();
		connectView.pack();
		connectView.setVisible(true);
	}

	private void hideConnectView() {
		logger.trace("Hide connect window");
		connectView.setVisible(false);
	}

	private void showLoginView() {
		logger.trace("Show login window");
		if (loginView == null) createLoginView();
		loginView.pack();
		loginView.setVisible(true);
	}

	private void hideLoginView() {
		logger.trace("Hide login window");
		loginView.setVisible(false);
	}

	private void showChatView() {
		logger.trace("Show chat window");
		if (chatView == null) createChatView();
		chatView.pack();
		chatView.setVisible(true);
	}

	private void hideChatView() {
		logger.trace("Hide chat window");
		chatView.dispose();
	}

	void connect(String host, int port) {
		logger.info("Connecting to {}:{}", host, port);
		for (ConnectHandler handler : connectHandlers) {
			if (!handler.connect(host, port)) {
				new AlertDialog(connectView, "Connect", "Couldn't connect to server");
				return;
			}
		}
		hideConnectView();
		showLoginView();
	}

	private void disconnect() {
		for (DisconnectHandler handler : disconnectHandlers) {
			handler.disconnect();
		}
	}

	void login(LoginPayload loginPayload) {
		logger.trace("Login user \"{}\"", loginPayload);
		createChatView();
		for (LoginHandler loginHandler : loginHandlers) {
			loginHandler.login(loginPayload);
		}
		chatView.addUser(client.getUser());
		showChatView();
		hideLoginView();
	}

	void logout() {
		logger.trace("Logging out");
		for (LogoutHandler logoutHandler : logoutHandlers) {
			logoutHandler.logout();
		}
		hideChatView();
		showLoginView();
	}

	@Override
	public void userEntered(User user) {
		chatView.addUser(user);
	}

	@Override
	public void userLeft(User user) {
		chatView.removeUser(user);
	}

	@Override
	public void userListReceived(User[] users) {
		chatView.removeAllUsers();
		for (User user : users) {
			chatView.addUser(user);
		}
	}

	void sendTextMessage(String text) {
		logger.trace("Sending text message \"{}\"", text.replaceAll("\\p{C}", "[]"));
		for (SendTextMessageHandler handler : sendTextMessageHandlers) {
			handler.sendTextMessage(text);
		}
	}

	@Override
	public void receive(Text msg) {
		chatView.appendMessage(msg.getText());
	}

	public void addConnectHandler(ConnectHandler handler) {
		connectHandlers.add(handler);
	}

	public void addDisconnectHandler(DisconnectHandler handler) {
		disconnectHandlers.add(handler);
	}

	public void addLoginHandler(LoginHandler handler) {
		loginHandlers.add(handler);
	}

	public void addLogoutHandler(LogoutHandler handler) {
		logoutHandlers.add(handler);
	}

	public void addSendTextMessageHandler(SendTextMessageHandler handler) {
		sendTextMessageHandlers.add(handler);
	}
}
