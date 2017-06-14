package ru.nsu.ccfit.bogush.network;

import ru.nsu.ccfit.bogush.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketMessageStream implements MessageSender, MessageReceiver {
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public SocketMessageStream(Socket socket) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}

	public void sendMessage(Message message) throws IOException {
		out.writeObject(message);
		out.flush();
	}

	public Message receiveMessage() throws IOException, ClassNotFoundException {
		return (Message) in.readObject();
	}
}
