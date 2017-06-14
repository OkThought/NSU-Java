package ru.nsu.ccfit.bogush.view;

public interface ChatEventHandler extends
		ConnectHandler,
		DisconnectHandler,
		LoginHandler,
		LogoutHandler,
		SendTextMessageHandler {

}
