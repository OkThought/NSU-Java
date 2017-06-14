package ru.nsu.ccfit.bogush.client;

import ru.nsu.ccfit.bogush.User;

public interface UserListChangeListener {
	void userEntered(User user);
	void userLeft(User user);
	void userListReceived(User[] users);
}
