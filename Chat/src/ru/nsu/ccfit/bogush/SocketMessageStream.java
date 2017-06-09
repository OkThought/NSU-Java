package ru.nsu.ccfit.bogush;

import ru.nsu.ccfit.bogush.msg.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketMessageStream implements MessageSender, MessageReceiver {
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public SocketMessageStream(Socket socket) throws IOException {
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
	}

	public void sendMessage(Message message) throws IOException {
		out.writeObject(message);
	}

	public Message receiveMessage() throws IOException, ClassNotFoundException {
		return (Message) in.readObject();
	}
}
