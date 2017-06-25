package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.MessageFactory;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import javax.xml.bind.annotation.*;
import java.util.Arrays;

@XmlRootElement(name = "success")
@XmlType(factoryClass = MessageFactory.class, factoryMethod = "createEmptyUserListSuccess")
public class UserListSuccess extends Success {
	private static final int MAX_USERS_TO_STRING = 3;
	private User[] users;

	public UserListSuccess(User[] users) {
		this.users = users;
	}

	@XmlElementWrapper(name = "listusers")
	@XmlElement(name = "user")
	public User[] getUsers() {
		return users;
	}

	public void setUsers(User[] users) {
		this.users = users;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		String result;
		if (users == null) {
			result = this.getClass().getSimpleName() + "(null)";
		} else if (users.length == 0) {
			result = this.getClass().getSimpleName() + "([])";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName());
			sb.append("([");
			for (int i = 0; i < users.length; i++) {
				if (i <= MAX_USERS_TO_STRING) {
					if (i != 0) {
						sb.append(", ");
					}
					sb.append('"');
					sb.append(users[i].getName());
					sb.append('"');
				} else {
					sb.append(" and ").append(users.length - MAX_USERS_TO_STRING).append(" more");
					break;
				}
			}
			sb.append("])");
			result = sb.toString();
		}
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserListSuccess that = (UserListSuccess) o;

		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(users, that.users);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(users);
	}
}
