package ru.nsu.ccfit.bogush.msg;

import ru.nsu.ccfit.bogush.User;

import java.util.Collection;

public class UserListMessage implements Message {
	private static final int MAX_USERS_TO_STRING = 3;
	private User[] users;

	public UserListMessage(User[] users) {
		this.users = users;
	}

	public UserListMessage(Collection<User> users) {
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
		if (users.length == 0) {
			result = "[]";
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (int i = 0; i < users.length; i++) {
				if (i <= MAX_USERS_TO_STRING) {
					if (i != 0) {
						sb.append(", ");
					}
					sb.append(users[i].getNickname());
				} else {
					sb.append(" and ").append(users.length - MAX_USERS_TO_STRING).append(" more");
					break;
				}
			}
			sb.append("]");
			result = sb.toString();
		}
		return result;
	}
}
