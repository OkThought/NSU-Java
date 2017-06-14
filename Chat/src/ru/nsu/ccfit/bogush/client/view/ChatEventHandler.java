package ru.nsu.ccfit.bogush.client.view;

public interface ChatEventHandler extends
		ConnectHandler,
		DisconnectHandler,
		LoginHandler,
		LogoutHandler,
		SendTextMessageHandler {

}
