package ru.nsu.ccfit.bogush.message;

import ru.nsu.ccfit.bogush.message.types.*;

public interface MessageHandler {
	void handle(Login message);
	void handle(Logout message);
	void handle(LoginSuccess message);
	void handle(LoginError message);
	void handle(LogoutSuccess message);
	void handle(LogoutError message);
	void handle(TextMessage message);
	void handle(UserList message);
	void handle(UserEntered message);
	void handle(UserLeft message);
	void handle(Message message);
}
