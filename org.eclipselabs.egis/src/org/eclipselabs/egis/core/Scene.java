package org.eclipselabs.egis.core;

import org.eclipselabs.egis.geometry.Bounds;
import org.eclipselabs.egis.map.Map;
import org.eclipselabs.egis.render.MapShift;

public class Scene {

	private final Map map;
	private Bounds viewport = new Bounds();
	private double rate = 1;
	private int viewWidth;
	private int viewHeight;
	public final MapShift shift = new MapShift();
	
	public Scene(Map map) {
		this.map = map;
	}

	public void fixAspectRatio() {
        double aspectWH = 1.0 * viewWidth / viewHeight;

        double eW = viewport.width();
        double eH = viewport.height();
        
        double inc = (eH * aspectWH - eW);
        double inc1 = inc / 2;
        viewport.x1 -= inc1;
        viewport.x2 += inc - inc1;
	}
	
	public void viewTo(Bounds consumedViewport) {
		this.viewport = consumedViewport;
		revalidate();
	}

	public void setSizeAndViewTo(int w, int h, Bounds consumedViewport) {
		if (w == 0 || h == 0) {
			return;
		}
		viewWidth = w;
		viewHeight = h;
		this.viewport = consumedViewport;
		revalidate();
	}
	
	public void setSize(int w, int h) {
		if (w == 0 || h == 0) {
			return;
		}
		viewWidth = w;
		viewHeight = h;
		revalidate();
	}

	public void revalidate() {
		fixAspectRatio();
		computeRate();
	}

	private void computeRate() {
		rate = viewWidth / (viewport.x2 - viewport.x1);
	}

	public int getViewWidth() {
		return viewWidth;
	}

	public int getViewHeight() {
		return viewHeight;
	}

	public Map getMap() {
		return map;
	}

	public double getRate() {
		return rate;
	}

	public int mapToScreenX(double x) {
		return (int) Math.round((x - viewport.x1) * rate);
	}

	public int mapToScreenY(double y) {
		return (int) Math.round((viewport.y2 - y) * rate);
	}

	public double screenToMapX(int x) {
		return viewport.x1 + x / rate;
	}

	public double screenToMapY(int y) {
		return viewport.y2 - y / rate;
	}

	public double scaleScreen(int value) {
		return value / rate;
	}
	
	public Bounds getViewport() {
		return viewport;
	}
	
	public void correlate(int sx, int sy, double mx, double my) {
		
		double x1 = mx - sx / rate;  
		double y2 = my + sy / rate;
		
		viewport.x2 -= viewport.x1 - x1;
		viewport.x1 = x1;
		viewport.y1 += y2 - viewport.y2;
		viewport.y2 = y2;
	}
}
