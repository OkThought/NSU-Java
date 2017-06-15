package ru.nsu.ccfit.bogush.message;

import ru.nsu.ccfit.bogush.message.types.*;
import ru.nsu.ccfit.bogush.message.types.ErrorMessage;

public interface MessageHandler {
	void handle(LoginRequest message);
	void handle(LoginEvent message);
	void handle(LoginSuccess message);

	void handle(LogoutRequest message);
	void handle(LogoutEvent message);
	void handle(LogoutSuccess message);

	void handle(ClientTextMessage message);
	void handle(ServerTextMessage message);

	void handle(UserListRequest message);
	void handle(UserListSuccess message);

	void handle(ErrorMessage message);

	void handle(Message message);
}
