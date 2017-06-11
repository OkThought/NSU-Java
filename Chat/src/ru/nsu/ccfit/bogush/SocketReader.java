package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.msg.Message;

import java.util.concurrent.LinkedBlockingQueue;

class SocketReader {
	private static final Logger logger = LogManager.getLogger();

	private Thread thread;
	private LinkedBlockingQueue<Message> messageQueue;

	SocketReader(MessageReceiver messageReceiver, int queueCapacity) {
		messageQueue = new LinkedBlockingQueue<>(queueCapacity);
		thread = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					Message msg = messageReceiver.receiveMessage();
					messageQueue.put(msg);
					logger.info("Received {}", msg);
				} catch (InterruptedException e) {
					logger.trace("Interrupted");
					break;
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Couldn't receive message");
					break;
				}
			}
		});
	}

	void start() {
		logger.trace("Start {}", SocketReader.class.getSimpleName());
		thread.start();
	}

	void stop() {
		logger.trace("Stop {}", SocketReader.class.getSimpleName());
		thread.interrupt();
	}

	Message read() throws InterruptedException {
		Message msg = messageQueue.take();
		logger.trace("Took {} from message queue", msg);
		return msg;
	}
}
