package ru.nsu.ccfit.bogush.chat.message;

import ru.nsu.ccfit.bogush.chat.message.types.*;

public interface MessageHandler {
	void handle(LoginRequest message);
	void handle(LoginEvent message);
	void handle(LoginSuccess message);

	void handle(LogoutRequest message);
	void handle(LogoutEvent message);

	void handle(TextMessageRequest message);
	void handle(TextMessageEvent message);

	void handle(UserListRequest message);
	void handle(UserListSuccess message);

	void handle(Success message);
	void handle(ErrorMessage message);

	void handle(Message message);
}
