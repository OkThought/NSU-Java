package ru.nsu.ccfit.bogush.chat.message;

import ru.nsu.ccfit.bogush.chat.User;
import ru.nsu.ccfit.bogush.chat.client.Client;
import ru.nsu.ccfit.bogush.chat.message.types.*;
import ru.nsu.ccfit.bogush.chat.network.Session;

import java.util.Collection;

public final class MessageFactory {
	private static final User EMPTY_USER = new User();
	private static final Session EMPTY_SESSION = Session.NO_SESSION;
	private static final String EMPTY_TEXT = "";
	private static final String DEFAULT_TYPE = Client.CHAT_VERSION;


	private MessageFactory() {}

	/**** Login ****/
	public static LoginRequest createLoginRequest(User user, String type) {
		return new LoginRequest(user, type);
	}

	public static LoginRequest createLoginRequest(String userName, String type) {
		return createLoginRequest(new User(userName), type);
	}

	public static LoginRequest createLoginRequest(User user) {
		return new LoginRequest(user, DEFAULT_TYPE);
	}

	public static LoginRequest createLoginRequest(String name) {
		return createLoginRequest(new User(name));
	}

	public static LoginRequest createEmptyLoginRequest() {
		return createLoginRequest(EMPTY_USER);
	}

	public static LoginEvent createLoginEvent(User user) {
		return new LoginEvent(user);
	}

	public static LoginEvent createLoginEvent(String name) {
		return createLoginEvent(new User(name));
	}

	public static LoginEvent createEmptyLoginEvent() {
		return createLoginEvent(EMPTY_USER);
	}

	public static LoginSuccess createLoginSuccess(Session session) {
		return new LoginSuccess(session);
	}

	public static LoginSuccess createLoginSuccess(int sessionId) {
		return createLoginSuccess(new Session(sessionId));
	}

	public static LoginSuccess createLoginSuccess(String sessionId) {
		return createLoginSuccess(Integer.parseInt(sessionId));
	}

	public static LoginSuccess createEmptyLoginSuccess() {
		return createLoginSuccess(EMPTY_SESSION);
	}

	/**** Logout ****/

	public static LogoutRequest createLogoutRequest(Session session) {
		return new LogoutRequest(session);
	}

	public static LogoutRequest createLogoutRequest(int sessionId) {
		return createLogoutRequest(new Session(sessionId));
	}

	public static LogoutRequest createLogoutRequest(String sessionId) {
		return createLogoutRequest(Integer.parseInt(sessionId));
	}

	public static LogoutRequest createEmptyLogoutRequest() {
		return createLogoutRequest(EMPTY_SESSION);
	}

	public static LogoutEvent createLogoutEvent(User user) {
		return new LogoutEvent(user);
	}

	public static LogoutEvent createLogoutEvent(String name) {
		return createLogoutEvent(new User(name));
	}

	public static LogoutEvent createEmptyLogoutEvent() {
		return createLogoutEvent(EMPTY_USER);
	}

	/**** User list ****/

	public static UserListRequest createUserListRequest(Session session) {
		return new UserListRequest(session);
	}

	public static UserListRequest createUserListRequest(int sessionId) {
		return createUserListRequest(new Session(sessionId));
	}

	public static UserListRequest createUserListRequest(String sessionId) {
		return createUserListRequest(Integer.parseInt(sessionId));
	}

	public static UserListRequest createEmptyUserListRequest() {
		return createUserListRequest(EMPTY_SESSION);
	}

	public static UserListSuccess createUserListSuccess(User[] users) {
		return new UserListSuccess(users);
	}

	public static UserListSuccess createEmptyUserListSuccess() {
		return createUserListSuccess((User[]) null);
	}

	public static UserListSuccess createUserListSuccess(Collection<User> users) {
		return createUserListSuccess(users.toArray(new User[users.size()]));
	}

	/**** Text message ****/

	public static TextMessageRequest createTextMessageRequest(String text, Session session) {
		return new TextMessageRequest(text, session);
	}

	public static TextMessageRequest createTextMessageRequest(String text, int sessionId) {
		return createTextMessageRequest(text, new Session(sessionId));
	}

	public static TextMessageRequest createTextMessageRequest(String text, String sessionId) {
		return createTextMessageRequest(text, Integer.parseInt(sessionId));
	}

	public static TextMessageRequest createEmptyTextMessageRequest() {
		return createTextMessageRequest(EMPTY_TEXT, EMPTY_SESSION);
	}

	public static TextMessageRequest createTextMessage(String text, Session session) {
		return createTextMessageRequest(text, session);
	}

	public static TextMessageRequest createTextMessage(String text, int sessionId) {
		return createTextMessageRequest(text, new Session(sessionId));
	}

	public static TextMessageEvent createTextMessageEvent(String text, User author) {
		return new TextMessageEvent(text, author);
	}

	public static TextMessageEvent createTextMessageEvent(String text, String authorName) {
		return new TextMessageEvent(text, new User(authorName));
	}

	public static TextMessageEvent createEmptyTextMessageEvent() {
		return createTextMessageEvent(EMPTY_TEXT, EMPTY_USER);
	}

	public static TextMessageEvent createTextMessage(String text, User author) {
		return createTextMessageEvent(text, author);
	}

	public static TextMessageEvent createTextMessage(String text, String author) {
		return createTextMessageEvent(text, new User(author));
	}

	/**** Success ****/

	public static Success createSuccess() {
		return new Success();
	}

	public static Success createEmptySuccess() {
		return createSuccess();
	}

	/**** Error ****/

	public static ErrorMessage createErrorMessage(String message) {
		return new ErrorMessage(message);
	}

	public static ErrorMessage createEmptyErrorMessage() {
		return createErrorMessage(EMPTY_TEXT);
	}
}
