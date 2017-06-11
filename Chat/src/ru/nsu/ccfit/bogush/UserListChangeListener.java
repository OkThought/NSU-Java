package ru.nsu.ccfit.bogush;

public interface UserListChangeListener {
	void userEntered(User user);
	void userLeft(User user);
	void userListReceived(User[] users);
}
