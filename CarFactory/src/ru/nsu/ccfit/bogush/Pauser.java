package ru.nsu.ccfit.bogush;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class Pauser {
	private boolean paused = false;
	private ArrayList<Pausable> pausables = new ArrayList<>();

	private static final String LOGGER_NAME = "Pauser";
	private static final Logger logger = LogManager.getLogger(LOGGER_NAME);

	public void addPausable(Pausable pausable) {
		if (pausable == null) {
			logger.trace("pausable is null, do nothing");
		} else {
			logger.trace("add pausable " + pausable);
			pausables.add(pausable);
		}
	}

	public void addPausables(Collection<Pausable> pausables) {
		logger.trace("add pausables " + pausables);
		this.pausables.addAll(pausables);
	}

	public void pause() {
		logger.trace("pause");
		if (!paused) {
			paused = true;
			for (Pausable pausable : pausables) {
				pausable.pause();
			}
		} else {
			logger.trace("already paused, do nothing");
		}
	}

	public void resume() {
		logger.trace("resume");
		if (paused) {
			paused = false;
			for (Pausable pausable : pausables) {
				pausable.resume();
			}
		} else {
			logger.trace("wasn't paused, do nothing");
		}
	}

	public void toggle() {
		logger.trace("toggle from " +
				(paused ? "paused":"resumed") + " to " +
				(paused ? "resumed":"paused"));
		if (paused) resume();
		else pause();
	}

	public boolean isPaused() {
		return paused;
	}

	public interface Pausable {
		void pause();
		void resume();
		boolean isPaused();
	}
}
