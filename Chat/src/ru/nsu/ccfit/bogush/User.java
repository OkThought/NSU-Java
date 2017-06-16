package ru.nsu.ccfit.bogush;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlRootElement(name = "user")
@XmlType
public class User implements Serializable {
	private String nickname;
	private String type;

	public User() {
		nickname = "";
		type = "";
	}

	public User(String nickname) {
		this.nickname = nickname;
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
}
