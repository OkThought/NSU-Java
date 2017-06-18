package ru.nsu.ccfit.bogush;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlRootElement(name = "user")
@XmlType
public class User implements Serializable {
	private static String defaultType = "";

	private String nickname;
	private String type;

	public User() {
		this("");
	}

	public User(String nickname) {
		this(nickname, defaultType);
	}

	public User(String nickname, String type) {
		this.nickname = nickname;
		this.type = type;
	}

	@XmlElement(name = "name")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@XmlElement(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return getNickname();
	}

	@Override
	public int hashCode() {
		return nickname != null ? nickname.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof User && ((User) obj).getNickname().equals(nickname);
	}

	public static String getDefaultType() {
		return defaultType;
	}

	public static void setDefaultType(String defaultType) {
		User.defaultType = defaultType;
	}
}
