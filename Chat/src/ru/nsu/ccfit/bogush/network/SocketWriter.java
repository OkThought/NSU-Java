package ru.nsu.ccfit.bogush.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.Message;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketWriter implements Runnable {
	private static final Logger logger = LogManager.getLogger(SocketWriter.class.getSimpleName());

	private Thread thread;
	private final MessageSender messageSender;
	private LinkedBlockingQueue<Message> messageQueue;

	public SocketWriter(MessageSender messageSender, int queueCapacity) {
		this.messageSender = messageSender;
		this.messageQueue = new LinkedBlockingQueue<>(queueCapacity);
		thread = new Thread(this, this.getClass().getSimpleName());
	}

	@Override
	public void run() {
		logger.trace("Started {}", this);
		while (!Thread.interrupted()) {
			try {
				Message msg = messageQueue.take();
				logger.info("Sending {}...", msg);
				messageSender.sendMessage(msg);
				logger.info("{} sent successfully", msg);
			} catch (MessageSender.Exception e) {
				logger.error("Failed to send message");
			} catch (InterruptedException e) {
				logger.trace("Interrupted {}", this);
				break;
			}
		}
	}

	public void start() {
		logger.trace("Starting {}", this);
		thread.start();
	}

	public void stop() {
		logger.trace("Stopping {}", this);
		thread.interrupt();
	}

	public void write(Message message) throws InterruptedException {
		logger.trace("Put {} to message queue", message);
		messageQueue.put(message);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
