package org.eclipselabs.egis.core;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.round;

import org.eclipselabs.egis.geometry.Bounds;
import org.eclipselabs.egis.geometry.Point;

/**
 * Can be generalizated to any TMS by parametize of constants.
 */
public class OSM {

	private static final double MERCATOR_WORLD_WIDTH = 2 * PI;
	public static final int TILE_SIZE = 256;
	
	public static int getLevel(Scene scene) {
		int viewWidth = scene.getViewWidth();
		int tilesCount;
		double wdx;
		if (viewWidth < TILE_SIZE) {
			tilesCount = 1;
			wdx = (TILE_SIZE - viewWidth) / scene.getRate() * -1;
		} else {
			tilesCount = viewWidth / TILE_SIZE;
			wdx = (viewWidth % TILE_SIZE) / scene.getRate();
		}
		double width = scene.getViewport().width() - wdx;
		double shownWorlds = MERCATOR_WORLD_WIDTH / width;
		int totalTiles = (int) round(shownWorlds * tilesCount);
		return lg(totalTiles) + 1; //???
	}

	public static void set(Scene scene, int level, double x, double y) {
		Bounds viewport = scene.getViewport();
		viewport.x1	= x;
		viewport.y1	= y;
		int viewWidth = scene.getViewWidth();
		int viewHeight = scene.getViewHeight();
		
		double ww = MERCATOR_WORLD_WIDTH * viewWidth / pow(2, level) * TILE_SIZE; 
		
		double rate = viewWidth / ww;
		double hh = viewHeight / rate;
		viewport.x2	= x + ww;
		viewport.y2	= y + hh;
		scene.revalidate();
	}
	
	public static Point getTileXY(double x, double y, int level) {
	    x = (1 + (x / PI)) / 2;
	    y = (1 - (y / PI)) / 2 ;
		int n = (int) round(pow(2, level)); //USE INTEGER POW
		x *= n;
		y *= n;
		return new Point(x, y);
	}

	
	private static int lg(int v) {
		int r = 0;
		while ((v >>= 1) != 0) {
		  r++;
		}
		return r;
	}
	
	public static void main(String[] args) {
		System.out.println(Math.log(Math.tan(Math.toRadians(85)) + 1 / Math.cos(Math.toRadians(85))));
	}
	
}
