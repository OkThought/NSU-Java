package ru.nsu.ccfit.bogush.threadpool;

public interface TaskSubscriber {
	void queueSizeChanged(int tasksAwaiting);
}
