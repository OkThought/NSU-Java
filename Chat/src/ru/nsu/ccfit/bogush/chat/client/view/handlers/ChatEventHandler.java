package ru.nsu.ccfit.bogush.chat.client.view.handlers;

public interface ChatEventHandler extends
		ConnectHandler,
		DisconnectHandler,
		LoginHandler,
		LogoutHandler,
		SendMessageHandler {

}
