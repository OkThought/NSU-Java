package ru.nsu.ccfit.bogush.client.view;

import ru.nsu.ccfit.bogush.network.LoginPayload;

public interface LoginHandler {
	void login(LoginPayload loginPayload);
}
