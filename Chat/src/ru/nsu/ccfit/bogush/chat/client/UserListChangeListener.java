package ru.nsu.ccfit.bogush.chat.client;

import ru.nsu.ccfit.bogush.chat.User;

public interface UserListChangeListener {
	void userEntered(User user);
	void userLeft(User user);
	void userListReceived(User[] users);
}
