package org.eclipselabs.egis.core;


public class OSMZoomStrategy implements ZoomStrategy {
	
	@Override
	public float in() {
		return 0.5f;
	}

	@Override
	public float out() {
		return 2;
	}
	
}
