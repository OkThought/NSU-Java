package ru.nsu.ccfit.bogush.chat.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.ccfit.bogush.chat.message.Message;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketReader implements Runnable {
	private static final Logger logger = LogManager.getLogger(SocketReader.class.getSimpleName());

	private Thread thread;
	private final MessageReceiver messageReceiver;
	private LinkedBlockingQueue<Message> messageQueue;
	private ArrayList<LostConnectionListener> lostConnectionListeners = new ArrayList<>();

	public SocketReader(MessageReceiver messageReceiver, LostConnectionListener lostConnectionListener, int queueCapacity) {
		this(messageReceiver, queueCapacity);
		addLostConnectionListener(lostConnectionListener);
	}

	public SocketReader(MessageReceiver messageReceiver, int queueCapacity) {
		this.messageReceiver = messageReceiver;
		messageQueue = new LinkedBlockingQueue<>(queueCapacity);
		initThread();
	}

	private void initThread() {
		thread = new Thread(this, this.getClass().getSimpleName());
	}

	public void addLostConnectionListener(LostConnectionListener listener) {
		lostConnectionListeners.add(listener);
	}

	@Override
	public void run() {
		logger.trace("Started {}", this);
		while (!Thread.interrupted()) {
			logger.info("Listening...");
			try {
				Message msg = messageReceiver.receiveMessage();
				logger.info("Received {}", msg);
				messageQueue.put(msg);
			} catch (InterruptedException e) {
				logger.info("Interrupted {}", this);
				break;
			} catch (MessageReceiver.Exception e) {
				logger.info("Lost connection");
				for (LostConnectionListener listener : lostConnectionListeners) {
					listener.lostConnection();
				}
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
		initThread();
	}

	public Message read() throws InterruptedException {
		Message msg = messageQueue.take();
		logger.trace("Took {} from message queue", msg);
		return msg;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
