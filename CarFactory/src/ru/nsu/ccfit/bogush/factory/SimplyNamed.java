package ru.nsu.ccfit.bogush.factory;

class SimplyNamed {
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
