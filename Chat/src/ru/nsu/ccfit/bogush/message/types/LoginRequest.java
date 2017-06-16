package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.network.LoginPayload;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "command")
@XmlType
public class LoginRequest implements Request {
	private LoginPayload loginPayload;

	@XmlAttribute(name = "name")
	private static final String messageType = "login";

	@Override
	public String getCommandName() {
		return messageType;
	}

	public LoginRequest() {
		loginPayload = new LoginPayload();
	}

	public LoginRequest(LoginPayload loginPayload) {
		this.loginPayload = loginPayload;
	}

	public LoginPayload getLoginPayload() {
		return loginPayload;
	}

	@XmlElement(name = "name")
	public void setNickname(String nickname) {
		loginPayload.setUser(new User(nickname));
	}

	@XmlElement(name = "type")
	public void setType(String type) {
		loginPayload.setType(type);
	}

	public String getNickname() {
		return loginPayload.getUser().getNickname();
	}

	public String getType() {
		return loginPayload.getType();
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + loginPayload + ")";
	}
}
