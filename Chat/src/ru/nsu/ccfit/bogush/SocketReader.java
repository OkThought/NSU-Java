package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.Message;

import java.util.concurrent.LinkedBlockingQueue;

public class SocketReader {
	private static final Logger logger = LogManager.getLogger();

	private Thread thread;
	private LinkedBlockingQueue<Message> messageQueue;

	public SocketReader(MessageReceiver messageReceiver, int queueCapacity) {
		messageQueue = new LinkedBlockingQueue<>(queueCapacity);
		thread = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					messageQueue.put(messageReceiver.receiveMessage());
				} catch (InterruptedException e) {
					logger.trace("Interrupted");
					break;
				} catch (Exception e) {
					logger.error("Couldn't receive message");
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

	public Message read() throws InterruptedException {
		return messageQueue.take();
	}
}
