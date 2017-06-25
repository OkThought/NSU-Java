package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.client.Client;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlRootElement(name = "user")
@XmlType
public class User implements Serializable {
	public static final String DEFAULT_TYPE = Client.TYPE;
	private String type;
	private String name;

	public User() {
		this("anonymous");
	}

	public User(String name) {
		this(name, DEFAULT_TYPE);
	}

	public User(String name, String type) {
		this.name = name;
		this.type = type;
	}

	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return getName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		return name != null ? name.equals(user.name) : user.name == null;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
