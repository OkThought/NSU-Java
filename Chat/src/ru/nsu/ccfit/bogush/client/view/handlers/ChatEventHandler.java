package ru.nsu.ccfit.bogush.client.view.handlers;

public interface ChatEventHandler extends
		ConnectHandler,
		DisconnectHandler,
		LoginHandler,
		LogoutHandler,
		SendMessageHandler {

}
