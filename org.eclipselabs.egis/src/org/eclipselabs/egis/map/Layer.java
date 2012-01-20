package org.eclipselabs.egis.map;

import org.eclipselabs.egis.map.layers.egformat.GeometryCursor;


public interface Layer {

	GeometryCursor queryGeometry(double x1, double y1, double x2, double y2);

	void dispose();
	
}
