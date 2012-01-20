package org.eclipselabs.egis.map;

import java.util.ArrayList;
import java.util.List;

public class Map {

	private final List<Layer> layers = new ArrayList<Layer>();

	public List<Layer> getLayers() {
		return layers;
	}

	public void dispose() {
		for (Layer layer : layers) {
			layer.dispose();
		}
	}
	
}
