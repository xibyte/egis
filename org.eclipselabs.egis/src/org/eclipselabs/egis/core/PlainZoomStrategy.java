package org.eclipselabs.egis.core;

public class PlainZoomStrategy implements ZoomStrategy {

	@Override
	public float in() {
		return 1 / 1.15f;
	}

	@Override
	public float out() {
		return 1.15f;
	}
}
