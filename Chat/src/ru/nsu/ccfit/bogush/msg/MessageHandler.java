package ru.nsu.ccfit.bogush.msg;

public interface MessageHandler {
	void handle(LoginMessage message);
	void handle(LogoutMessage message);
	void handle(SuccessMessage message);
	void handle(ErrorMessage message);
	void handle(TextMessage message);
	void handle(UserListMessage message);
	void handle(UserEnteredMessage message);
	void handle(UserLeftMessage message);
	void handle(Message message);
}
