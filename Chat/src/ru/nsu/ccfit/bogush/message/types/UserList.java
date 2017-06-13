package ru.nsu.ccfit.bogush.message.types;

import ru.nsu.ccfit.bogush.User;
import ru.nsu.ccfit.bogush.message.Message;
import ru.nsu.ccfit.bogush.message.MessageHandler;

import java.util.Collection;

public class UserList implements Message {
	private static final int MAX_USERS_TO_STRING = 3;
	private User[] users;

	public UserList() {}

	public UserList(User[] users) {
		this.users = users;
	}

	public UserList(Collection<User> users) {
		this.users = users.toArray(new User[users.size()]);
	}

	public User[] getUsers() {
		return users;
	}

	@Override
	public void handleBy(MessageHandler handler) {
		handler.handle(this);
	}

	@Override
	public String toString() {
		String result;
		if (users == null) {
			result = this.getClass().getSimpleName() + "(request)";
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
					sb.append(users[i].getNickname());
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
}
