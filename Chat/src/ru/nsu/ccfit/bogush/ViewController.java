package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewController {
	private static final Logger logger = LogManager.getLogger();

	private LoginView loginView;
	private ChatView chatView;

	public ViewController() {
		loginView = new LoginView();
		loginView.setVisible(true);
		loginView.addLoginHandler(message -> {
			loginView.setVisible(false);
			if (chatView == null) {
				createChatView();
			}
			chatView.setVisible(true);
		});
	}

	private void createChatView() {
		chatView = new ChatView();
		chatView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				loginView.dispose();
			}
		});
	}
}
