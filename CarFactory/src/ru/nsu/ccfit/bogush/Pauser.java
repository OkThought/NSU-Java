package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class Pauser {
	private boolean paused = false;
	private final ArrayList<Pausable> pausables = new ArrayList<>();

	private static final String LOGGER_NAME = "Pauser";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public void addPausable(Pausable pausable) {
		logger.traceEntry();
		if (pausable == null) {
			logger.trace("pausable is null, do nothing");
		} else {
			logger.trace("add pausable " + pausable);
			pausables.add(pausable);
		}
		logger.traceExit();
	}

	public void addPausables(Collection<Pausable> pausables) {
		logger.traceEntry();
		logger.trace("add pausables " + pausables);
		this.pausables.addAll(pausables);
		logger.traceExit();
	}

	public void pause() {
		logger.traceEntry();
		logger.trace("pause");
		if (!paused) {
			paused = true;
			for (Pausable pausable : pausables) {
				pausable.pause();
			}
		} else {
			logger.trace("already paused, do nothing");
		}
		logger.traceExit();
	}

	public void resume() {
		logger.traceEntry();
		logger.trace("resume");
		if (paused) {
			paused = false;
			for (Pausable pausable : pausables) {
				pausable.resume();
			}
		} else {
			logger.trace("wasn't paused, do nothing");
		}
		logger.traceExit();
	}

	public void toggle() {
		logger.traceEntry();
		logger.trace("toggle from " +
				(paused ? "paused":"resumed") + " to " +
				(paused ? "resumed":"paused"));
		if (paused) resume();
		else pause();
		logger.traceExit();
	}

	public boolean isPaused() {
		logger.traceEntry();
		return logger.traceExit(paused);
	}

	public interface Pausable {
		void pause();
		void resume();
		boolean isPaused();
	}
}
