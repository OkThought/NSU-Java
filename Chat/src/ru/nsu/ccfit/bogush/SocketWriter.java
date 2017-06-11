package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.Message;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

class SocketWriter {
	private static final Logger logger = LogManager.getLogger();

	private Thread thread;
	private LinkedBlockingQueue<Message> messageQueue;

	SocketWriter(MessageSender messageSender, int queueCapacity) {
		this.messageQueue = new LinkedBlockingQueue<>(queueCapacity);
		thread = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					Message msg = messageQueue.take();
					messageSender.sendMessage(msg);
					logger.info("Send {}", msg);
				} catch (IOException e) {
					logger.error("Couldn't send message");
				} catch (InterruptedException e) {
					logger.trace("Interrupted");
					break;
				}
			}
		});
	}

	void start() {
		logger.trace("Start {}", SocketWriter.class.getSimpleName());
		thread.start();
	}

	void stop() {
		logger.trace("Stop {}", SocketWriter.class.getSimpleName());
		thread.interrupt();
	}

	void write(Message message) throws InterruptedException {
		logger.trace("Put {} to message queue", message);
		messageQueue.put(message);
	}
}
