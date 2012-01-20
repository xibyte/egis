package org.eclipselabs.egis.geometry;

public class Bounds {

	public double x1;
	public double y1;
	public double x2;
	public double y2;

	public Bounds() {
		this.x1 = Double.MAX_VALUE;
		this.y1 = Double.MAX_VALUE;
		this.x2 = -Double.MAX_VALUE;
		this.y2 = -Double.MAX_VALUE;
	}
	
	public Bounds(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public double width() {
		return x2 - x1;
	}
	
	public double height() {
		return y2 - y1;
	}

	public void setWidth(double width) {
		x2 = x1 + width;
	}
	
	public void setHeight(double height) {
		y2 = y1 + height;
	}
	
	public void scaleAtCentre(float value) {
		double olx = width();
		double nlx = olx * value;
		double dx = (nlx - olx) / 2; 
		x1 -= dx; 
		x2 += dx;
		
		double oly = height();
		double nly = oly * value;
		double dy = (nly - oly) / 2; 
		y1 -= dy; 
		y2 += dy; 
	}

	public void scaleAtBegin(double value) {
		double lx = width();
		x2 += lx * value -lx;

		double ly = height();
		y2 += ly * value -ly;
	}

	
	public void translate(double dx, double dy) {
		x1 += dx;
		y1 += dy;
		x2 += dx;
		y2 += dy;
	}
	
	public Point centre() {
		return new Point((x1 + x2) / 2, (y1 + y2) / 2);
	}
	
	public Bounds copy() {
		return new Bounds(x1, y1, x2, y2);
	}
}
