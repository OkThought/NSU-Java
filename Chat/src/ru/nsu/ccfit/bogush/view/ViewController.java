package ru.nsu.ccfit.bogush.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ViewController {
	private static final Logger logger = LogManager.getLogger();

	private ConnectView connectView;
	private LoginView loginView;
	private ChatView chatView;

	private ArrayList<LoginHandler> loginHandlers = new ArrayList<>();

	public ViewController() {
		createConnectView();
		connectView.pack();
	}

	private void createConnectView() {
		logger.trace("creating connect window");
		connectView = new ConnectView();
		connectView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				logger.trace("connect window closed");
			}
		});
	}

	private void createLoginView() {
		logger.trace("creating login window");
		loginView = new LoginView();
		loginView.setVisible(true);
		addLoginHandler(message -> {
			loginView.dispose();
			if (chatView == null) {
				createChatView();
			}
			chatView.pack();
		});

		loginView.setLoginHandlers(loginHandlers);

		loginView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				logger.trace("login window closed");
				connectView.pack();
			}
		});
	}

	private void createChatView() {
		logger.trace("creating chat window");
		chatView = new ChatView();
		chatView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				logger.trace("chat window closed");
				loginView.pack();
			}
		});
	}

	public void addLoginHandler(LoginHandler loginHandler) {
		loginHandlers.add(loginHandler);
	}
}
