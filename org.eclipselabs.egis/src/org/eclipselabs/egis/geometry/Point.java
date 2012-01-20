package org.eclipselabs.egis.geometry;

import java.io.DataOutput;
import java.io.IOException;

public class Point implements Geometry {

	public double x;
	public double y;
	
	public Point() {
	}

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int getMemorySize() {
		return 0;
	}

	@Override
	public void expandBounds(Bounds bounds) {
	}

	@Override
	public void toBinary(DataOutput out) throws IOException {
	}

	@Override
	public void accept(Visitor visitor) {
	}

}
