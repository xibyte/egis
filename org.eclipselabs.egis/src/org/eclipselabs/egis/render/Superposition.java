package org.eclipselabs.egis.render;

public class Superposition {

	public int srcX;
	public int srcY;
	public int dstX;
	public int dstY;
	public int width;
	public int height;
	
	public static Superposition make(int shiftX, int shiftY, 
			int viewW, int viewH,
			int imageW, int imageH,
			int restrictionX, int restrictionY, int restrictionW,
			int restrictionH) {
		
		Superposition sp = new Superposition(); 
		
		if (shiftX > 0) {
			sp.srcX = 0;
			sp.dstX = shiftX;
			sp.width = viewW - shiftX;
		} else {
			sp.srcX = -shiftX;
			sp.dstX = 0;
			sp.width = viewW - (-shiftX);
		}
		
		if (shiftY > 0) {
			sp.srcY = 0;
			sp.dstY = shiftY;
			sp.height = viewH - shiftY;
		} else {
			sp.srcY = -shiftY;
			sp.dstY = 0;
			sp.height = viewH - (-shiftY);
		}

		if (sp.width > imageW) {
			sp.width = imageW;
		}

		if (sp.height > imageH) {
			sp.height = imageH;
		}

		
		//Clip to requested bounds
		if (restrictionX > sp.dstX) {
			int dx = restrictionX - sp.dstX;
			sp.srcX += dx;
			sp.width -= dx;
			sp.dstX = restrictionX;
		}

		if (restrictionY > sp.dstY) {
			int dy = restrictionY - sp.dstY;
			sp.srcY += dy;
			sp.height -= dy;
			sp.dstY = restrictionY;
		}

		if (sp.width > restrictionW) {
			sp.width = restrictionW;
		}
		if (sp.height > restrictionH) {
			sp.height = restrictionH;
		}
		return sp;
	}

	public boolean isPositive() {
		return width > 0 && height > 0;
	}
	
}
