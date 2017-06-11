package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.msg.*;

public class ClientMessageHandler implements MessageHandler {
	private Client client;
	private LoginPayload loginPayload;

	public ClientMessageHandler(Client client) {
		this.client = client;
	}

	@Override
	public void handle(LoginMessage message) {

	}

	@Override
	public void handle(LogoutMessage message) {

	}

	@Override
	public void handle(SuccessMessage message) {

	}

	@Override
	public void handle(ErrorMessage message) {

	}

	@Override
	public void handle(TextMessage message) {

	}

	@Override
	public void handle(UserListMessage message) {

	}

	@Override
	public void handle(Message message) {

	}
}
