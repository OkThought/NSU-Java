package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.Message;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketWriter {
	private static final Logger logger = LogManager.getLogger();

	private Thread thread;
	private LinkedBlockingQueue<Message> messageQueue;

	public SocketWriter(MessageSender messageSender, int queueCapacity) {
		this.messageQueue = new LinkedBlockingQueue<>(queueCapacity);
		thread = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					messageSender.sendMessage(messageQueue.take());
				} catch (IOException e) {
					logger.error("Couldn't send message");
				} catch (InterruptedException e) {
					logger.trace("Interrupted");
					break;
				}
			}
		});
	}

	public void start() {
		logger.trace("Start");
		thread.start();
	}

	public void stop() {
		logger.trace("Stop");
		thread.interrupt();
	}

	public void write(Message message) throws InterruptedException {
		messageQueue.put(message);
	}
}
