package org.eclipselabs.egis.render;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipselabs.egis.core.Scene;

public class BufferedRenderService {

	private final Scene scene;
	private final Renderer renderer;
	private Image image;
	private GC gc;
	private Size size;
	
	public BufferedRenderService(Scene scene, Renderer renderer,
			int initWidth, int initHeight) {
		this.scene = scene;
		this.renderer = renderer;
		allocateImage(initWidth, initHeight);
		
	}
	
	private void allocateImage(int width, int height) {
		size = new Size(width, height);
		System.out.println("Allocated "+width + ":"+ height);
		image = new Image(null, width, height);
		gc = new GC(image);
	}

	public Image getBufferedImage() {
		return image;
	}
	
	public void render(Renderer.Handler handler) {
		ensureImageSize();
		renderer.render(scene, gc, handler);
	}

	private static final int MIN_INCREASE = 150;
	
	private void ensureImageSize() {
		
		boolean needReallocate = false;
		int iw = size.width;
		int sw = scene.getViewWidth();
		if (iw < sw) { 
			if (sw - iw < MIN_INCREASE) {
				iw += MIN_INCREASE;
			} else {
				iw = sw;
			}
			needReallocate = true;
		}
		
		int ih = size.height;
		int sh = scene.getViewHeight();
		if (ih < sh) {
			if (sh - ih < MIN_INCREASE) {
				ih += MIN_INCREASE;
			} else {
				ih = sh;
			}
			needReallocate = true;
		}
		if (needReallocate) {
			disposeResources();
			allocateImage(iw, ih);
		}
	}

	private void disposeResources() {
		image.dispose();
		gc.dispose();
	}
	
	public void dispose() {
		disposeResources();
	}

	public Size getBufferSize() {
		return size;
	}
	
	public static class Size {
		
		public final int width;
		public final int height;

		public Size(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
}
