package ru.nsu.ccfit.bogush;

public class Session {
	private int id;

	public Session(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static final Session NO_SESSION = new Session(0);
}
