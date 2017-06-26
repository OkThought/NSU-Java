package ru.nsu.ccfit.bogush.chat.serialization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import ru.nsu.ccfit.bogush.chat.User;
import ru.nsu.ccfit.bogush.chat.message.Message;
import ru.nsu.ccfit.bogush.chat.message.MessageFactory;
import ru.nsu.ccfit.bogush.chat.message.types.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.SocketException;

public class XMLMessageSerializer implements Serializer<Message> {
	private static final Logger logger = LogManager.getLogger(XMLMessageSerializer.class.getSimpleName());
	private static final String REQUEST_TAG                         = "command";
	private static final String EVENT_TAG                           = "event";
	private static final String SUCCESS_TAG                         = "success";
	private static final String ERROR_TAG                           = "error";

	private static final String ERROR_MESSAGE_TAG                   = "message";
	private static final String EVENT_NAME_ATTRIBUTE                = "name";
	private static final String LOGIN_REQUEST_NAME                  = "login";
	private static final String LOGOUT_REQUEST_NAME                 = "logout";
	private static final String USER_LIST_REQUEST_NAME              = "list";
	private static final String TEXT_MESSAGE_REQUEST_NAME           = "message";
	private static final String LOGIN_EVENT_NAME                    = "userlogin";
	private static final String LOGOUT_EVENT_NAME                   = "userlogout";
	private static final String MESSAGE_EVENT_NAME                  = "message";
	private static final String LOGIN_EVENT_NAME_TAG                = "name";
	private static final String LOGOUT_EVENT_NAME_TAG               = "name";
	private static final String TEXT_MESSAGE_EVENT_MESSAGE_TAG      = "message";
	private static final String TEXT_MESSAGE_EVENT_AUTHOR_TAG       = "name";
	private static final String REQUEST_NAME_ATTR                   = "name";
	private static final String LOGIN_REQUEST_NAME_TAG              = "name";
	private static final String LOGIN_REQUEST_TYPE_TAG              = "type";
	private static final String LOGOUT_REQUEST_SESSION_TAG          = "session";
	private static final String USER_LIST_REQUEST_SESSION_TAG       = "session";
	private static final String TEXT_MESSAGE_REQUEST_MESSAGE_TAG    = "message";
	private static final String TEXT_MESSAGE_REQUEST_SESSION_TAG    = "session";
	private static final String SUCCESS_SESSION_TAG                 = "session";
	private static final String SUCCESS_USER_LIST_TAG               = "listusers";
	private static final String USER_NAME_TAG                       = "name";
	private static final String USER_TYPE_TAG                       = "type";
	private static final int    DEFAULT_SIZE                        = 1 << 20; // megabyte
	private InputStream in;
	private OutputStream out;
	private Marshaller marshaller;
	private DocumentBuilder documentBuilder;
	private OutputStreamWriter outputStreamWriter;
	private DataOutputStream dataOutputStream;

	XMLMessageSerializer(InputStream in, OutputStream out, JAXBContext jaxbContext) throws SerializerException {
		this.in = in;
		this.out = out;
		outputStreamWriter = new OutputStreamWriter(out);
		dataOutputStream = new DataOutputStream(out);
		try {
			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = dbFactory.newDocumentBuilder();
		} catch (JAXBException | ParserConfigurationException e) {
			throw new SerializerException(e);
		}
	}

	@Override
	public void serialize(Message msg) throws SerializerException {
		logger.trace("Serializing {}", msg);
		StringWriter stringWriter = new StringWriter();
		try {
			marshaller.marshal(msg, stringWriter);
			logger.trace("Serialized into buffer");
		} catch (JAXBException e) {
			logger.error("Failed to serialize");
			throw new SerializerException(e);
		}

		String xml = stringWriter.toString();
		int size = xml.getBytes().length;
		writeSize(size);
		writeXml(xml);
	}

	private void writeSize(int size) {
		logger.trace("Writing size: {}", size);
		try {
			dataOutputStream.writeInt(size);
			dataOutputStream.flush();
			logger.trace("Size written");
		} catch (IOException e) {
			logger.error("Failed to write size");
			logger.catching(e);
		}
	}

	private void writeXml(String xml) {
		logger.trace("Writing xml:\n{}", xml);
		try {
			outputStreamWriter.write(xml);
			outputStreamWriter.flush();
			logger.trace("Xml written");
		} catch (IOException e) {
			logger.error("Failed to write xml");
			logger.catching(e);
		}
	}

	@Override
	public Message deserialize() throws SerializerException {
		logger.trace("Deserializing input stream...");
		int size = DEFAULT_SIZE;
		try {
			size = readSize();
			logger.trace("Read size: {}", size);
		} catch (SocketException | EOFException e) {
			throw new SerializerException(e);
		} catch (IOException e) {
			logger.error("Failed to read size");
		}

		String xml;
		try {
			xml = readXml(size);
			logger.trace("Read xml: \n{}", xml);
		} catch (SocketException | EOFException e) {
			throw new SerializerException(e);
		} catch (IOException e) {
			logger.error("Failed to read xml");
			return null;
		}

		try {
			ByteArrayInputStream xmlInputStream = new ByteArrayInputStream(xml.getBytes());
			Document doc = documentBuilder.parse(xmlInputStream);
			Element root = doc.getDocumentElement();
			if (root == null) {
				throw new SerializerException("Root tag not found");
			}
			String tag = root.getTagName();
			switch (tag) {
				case ERROR_TAG:
					return deserializeErrorMessage(root);
				case EVENT_TAG:
					return deserializeEvent(root);
				case REQUEST_TAG:
					return deserializeRequest(root);
				case SUCCESS_TAG:
					return deserializeSuccess(root);
				default:
					SerializerException e = new SerializerException("Unknown root tag: " + tag);
					logger.throwing(e);
					throw e;
			}
		} catch (SAXException | IOException e) {
			throw new SerializerException(e);
		}
	}

	private int readSize() throws IOException {
		logger.trace("Reading size...");
		DataInputStream dataInputStream = new DataInputStream(in);
		return dataInputStream.readInt();
	}

	private String readXml(int size) throws IOException {
		logger.trace("Reading {} bytes of xml...", size);
		byte[] bytes = new byte[size];
		if (-1 == in.read(bytes)) {
			logger.error("Eof found while reading xml");
			throw new EOFException();
		}
		return new String(bytes);
	}

	private ErrorMessage deserializeErrorMessage(Element errorElement) throws SerializerException {
		if (errorElement.hasChildNodes()) {
			Element messageElement = getElementByTagName(errorElement, ERROR_MESSAGE_TAG);
			String text = messageElement.getTextContent();
			return new ErrorMessage(text);
		}
		throw new SerializerException("No content in " + ErrorMessage.class.getSimpleName());
	}

	private Event deserializeEvent(Element eventElement) throws SerializerException {
		if (eventElement.hasAttributes()) {
			String eventName = eventElement.getAttribute(EVENT_NAME_ATTRIBUTE);
			if (!eventName.isEmpty()) {
				switch (eventName) {
					case LOGIN_EVENT_NAME:   return deserializeLoginEvent(eventElement);
					case LOGOUT_EVENT_NAME:  return deserializeLogoutEvent(eventElement);
					case MESSAGE_EVENT_NAME: return deserializeTextMessageEvent(eventElement);
					default: throw new SerializerException("Unknown event " +
							EVENT_NAME_ATTRIBUTE + ": " + eventName);
				}
			}
		}
		throw new SerializerException("Attribute '" + EVENT_NAME_ATTRIBUTE + "' not found");
	}

	private LoginEvent deserializeLoginEvent(Element loginEventElement) throws SerializerException {
		if (loginEventElement.hasChildNodes()) {
			Element loggedInUser = getElementByTagName(loginEventElement, LOGIN_EVENT_NAME_TAG);
			return MessageFactory.createLoginEvent(loggedInUser.getTextContent());
		}
		throw new SerializerException("No content in " + LoginEvent.class.getSimpleName());
	}

	private LogoutEvent deserializeLogoutEvent(Element logoutEventElement) throws SerializerException {
		if (logoutEventElement.hasChildNodes()) {
			Element loggedOutUser = getElementByTagName(logoutEventElement, LOGOUT_EVENT_NAME_TAG);
			return MessageFactory.createLogoutEvent(loggedOutUser.getTextContent());
		}
		throw new SerializerException("No content in " + LogoutEvent.class.getSimpleName());
	}

	private TextMessageEvent deserializeTextMessageEvent(Element textMessageElement) throws SerializerException {
		if (textMessageElement.hasChildNodes()) {
			Element message = getElementByTagName(textMessageElement, TEXT_MESSAGE_EVENT_MESSAGE_TAG);
			Element author = getElementByTagName(textMessageElement, TEXT_MESSAGE_EVENT_AUTHOR_TAG);
			String text = message.getTextContent();
			String authorName = author.getTextContent();
			return MessageFactory.createTextMessageEvent(text, authorName);
		}
		throw new SerializerException("No content in " + TextMessageEvent.class.getSimpleName());
	}

	private Request deserializeRequest(Element requestElement) throws SerializerException {
		if (requestElement.hasAttributes()) {
			String commandName = requestElement.getAttribute(REQUEST_NAME_ATTR);
			if (commandName.isEmpty()) throw new SerializerException("Command name not found");
			switch (commandName) {
				case LOGIN_REQUEST_NAME:        return deserializeLoginRequest(requestElement);
				case LOGOUT_REQUEST_NAME:       return deserializeLogoutRequest(requestElement);
				case USER_LIST_REQUEST_NAME:    return deserializeUserListRequest(requestElement);
				case TEXT_MESSAGE_REQUEST_NAME: return deserializeTextMessageRequest(requestElement);
				default: throw new SerializerException("Unknown command name: " + commandName);
			}
		}
		throw new SerializerException("No attributes in " + Request.class.getSimpleName());
	}

	private LoginRequest deserializeLoginRequest(Element loginRequestElement) throws SerializerException {
		if (loginRequestElement.hasChildNodes()) {
			Element nameElement = getElementByTagName(loginRequestElement, LOGIN_REQUEST_NAME_TAG);
			Element typeElement = getElementByTagName(loginRequestElement, LOGIN_REQUEST_TYPE_TAG);
			String name = nameElement.getTextContent();
			String type = typeElement.getTextContent();
			return MessageFactory.createLoginRequest(name, type);
		}
		throw new SerializerException("No content in " + LoginRequest.class.getSimpleName());
	}

	private LogoutRequest deserializeLogoutRequest(Element logoutRequestElement) throws SerializerException {
		if (logoutRequestElement.hasChildNodes()) {
			Element sessionElement = getElementByTagName(logoutRequestElement, LOGOUT_REQUEST_SESSION_TAG);
			String session = sessionElement.getTextContent();
			return MessageFactory.createLogoutRequest(session);
		}
		throw new SerializerException("No content in " + LogoutRequest.class.getSimpleName());
	}

	private UserListRequest deserializeUserListRequest(Element userListRequestElement) throws SerializerException {
		if (userListRequestElement.hasChildNodes()) {
			Element sessionElement = getElementByTagName(userListRequestElement, USER_LIST_REQUEST_SESSION_TAG);
			String session = sessionElement.getTextContent();
			return MessageFactory.createUserListRequest(session);
		}
		throw new SerializerException("No content in " + UserListRequest.class.getSimpleName());
	}

	private TextMessageRequest deserializeTextMessageRequest(Element textMessageRequstElement) throws SerializerException {
		if (textMessageRequstElement.hasChildNodes()) {
			Element messageElement = getElementByTagName(textMessageRequstElement, TEXT_MESSAGE_REQUEST_MESSAGE_TAG);
			Element sessionElement = getElementByTagName(textMessageRequstElement, TEXT_MESSAGE_REQUEST_SESSION_TAG);
			String message = messageElement.getTextContent();
			String session = sessionElement.getTextContent();
			return MessageFactory.createTextMessageRequest(message, session);
		}
		throw new SerializerException("No content in " + TextMessageRequest.class.getSimpleName());
	}

	private Success deserializeSuccess(Element successElement) throws SerializerException {
		if (successElement.hasChildNodes()) {
			NodeList sessionElements = successElement.getElementsByTagName(SUCCESS_SESSION_TAG);
			NodeList userListElements = successElement.getElementsByTagName(SUCCESS_USER_LIST_TAG);
			int sessionElementsCount = sessionElements.getLength();
			int userListElementsCount = userListElements.getLength();
			if (sessionElementsCount == 1 && userListElementsCount == 0) {
				Element sessionElement = asElementChecked(sessionElements.item(0));
				String session = sessionElement.getTextContent();
				return MessageFactory.createLoginSuccess(session);
			} else if (sessionElementsCount == 0 && userListElementsCount == 1) {
				Element userListElement = asElementChecked(userListElements.item(0));
				return deserializeUserListSuccess(userListElement);
			}
			throw new SerializerException("Bad number of elements (" +
					SUCCESS_SESSION_TAG + ": " + sessionElementsCount + ", " +
					SUCCESS_USER_LIST_TAG + ": " + userListElementsCount + " found");
		} else {
			return MessageFactory.createSuccess();
		}
	}

	private UserListSuccess deserializeUserListSuccess(Element userListElement) throws SerializerException {
		NodeList nodeList = userListElement.getElementsByTagName("user");
		User[] users = new User[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element userElement = asElementChecked(nodeList.item(i));
			users[i] = deserializeUser(userElement);
		}
		return MessageFactory.createUserListSuccess(users);
	}

	private User deserializeUser(Element userElement) throws SerializerException {
		if (userElement.hasChildNodes()) {
			Element nameElement = getElementByTagName(userElement, USER_NAME_TAG);
			Element typeElement = getElementByTagName(userElement, USER_TYPE_TAG);
			String name = nameElement.getTextContent();
			String type = typeElement.getTextContent();
			return new User(name, type);
		}
		throw new SerializerException("Elements not found");
	}

	private Element asElement(Node node) {
		return node.getNodeType() == Node.ELEMENT_NODE ? (Element) node : null;
	}

	private Element asElementChecked(Node node) throws SerializerException {
		Element element = asElement(node);
		if (element == null) throw new SerializerException("Expected element, but found: " + node.getNodeType());
		return element;
	}

	private Element getElementByTagName(Element src, String tag) throws SerializerException {
		NodeList nodeList = src.getElementsByTagName(tag);
		switch (nodeList.getLength()) {
			case 0: throw new SerializerException("Element with tag '" + tag + "' not found");
			case 1: return asElementChecked(nodeList.item(0));
			default: throw new SerializerException("Many elements with tag '" + tag + "' found:" + nodeList.getLength());
		}
	}

	@Override
	public Type getType() {
		return Type.XML_SERIALIZER;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
