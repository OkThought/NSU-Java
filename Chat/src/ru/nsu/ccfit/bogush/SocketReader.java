package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.message.Message;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

class SocketReader implements Runnable {
	private static final Logger logger = LogManager.getLogger(SocketReader.class.getSimpleName());

	private Thread thread;
	private final MessageReceiver messageReceiver;
	private LinkedBlockingQueue<Message> messageQueue;
	private ArrayList<LostConnectionListener> lostConnectionListeners = new ArrayList<>();

	SocketReader(MessageReceiver messageReceiver, LostConnectionListener lostConnectionListener, int queueCapacity) {
		this(messageReceiver, queueCapacity);
		addLostConnectionListener(lostConnectionListener);
	}

	SocketReader(MessageReceiver messageReceiver, int queueCapacity) {
		this.messageReceiver = messageReceiver;
		messageQueue = new LinkedBlockingQueue<>(queueCapacity);
		thread = new Thread(this, this.getClass().getSimpleName());
	}

	public void addLostConnectionListener(LostConnectionListener listener) {
		lostConnectionListeners.add(listener);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Message msg = messageReceiver.receiveMessage();
				messageQueue.put(msg);
				logger.info("Received {}", msg);
			} catch (InterruptedException e) {
				logger.trace("Interrupted");
				break;
			} catch (EOFException e) {
				logger.info("Lost connection");
				for (LostConnectionListener listener : lostConnectionListeners) {
					listener.lostConnection();
				}
				break;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Couldn't receive message");
				break;
			}
		}
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
