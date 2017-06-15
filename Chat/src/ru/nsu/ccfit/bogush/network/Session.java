package ru.nsu.ccfit.bogush.network;

import java.io.Serializable;

public class Session implements Serializable {
	public static final int NO_SESSION_ID = 0;
	public static final Session NO_SESSION = new Session(NO_SESSION_ID);

	private int id;

	public Session() {
		this(NO_SESSION_ID);
	}

	public Session(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Session session = (Session) o;

		return id == session.id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(id: " + getId() + ")";
	}
}
