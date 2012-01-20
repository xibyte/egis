package org.eclipselabs.egis.core;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipselabs.egis.geometry.Bounds;
import org.eclipselabs.egis.render.BufferedRenderService;
import org.eclipselabs.egis.ui.EditorRenderHandler;

/**
 * Common useful operations with map.
 */
public class Controller {
	
	private final Scene scene;
	private final Control mapControl;
	private final BufferedRenderService renderService;
	private ZoomStrategy zoomStrategy;
	private EditorRenderHandler rhandler;

	
	public Controller(Scene scene, Control mapControl,
			BufferedRenderService renderService) {
		this.scene = scene;
		this.mapControl = mapControl;
		this.renderService = renderService;
		zoomStrategy = new PlainZoomStrategy();
	}

	public void zoomIn(int x, int y) {
		zoom(zoomStrategy.in(), x, y);
	}

	public void zoomOut(int x, int y) {
		zoom(zoomStrategy.out(), x, y);
	}

	public void zoom(float value, int scrX, int scrY) {
		double x = scene.screenToMapX(scrX); 
		double y = scene.screenToMapY(scrY);
		Bounds viewport = scene.getViewport();
		viewport.scaleAtBegin(value);
		scene.revalidate();
		scene.correlate(scrX, scrY, x, y);
		refreshBufferAndRedrawControl();
	}

	private void refreshBufferAndRedrawControl() {
		renderBuffer();
		redraw();
	}
	
	public void adjustSize() {
		Point size = mapControl.getSize();
		scene.setSize(size.x, size.y);
		refreshBufferAndRedrawControl();
	}
	
	public void redraw() {
		mapControl.redraw();
	}

	public void redraw(Bounds bounds) {
		redraw(bounds.x1, bounds.y1, bounds.x2, bounds.y2);
	}
	
	public void redraw(double x1, double y1, double x2, double y2) {
		int x = scene.mapToScreenX(x1);
		int y = scene.mapToScreenY(y1);
		int w = scene.mapToScreenX(x2) - x;
		int h = scene.mapToScreenY(y2 )- y;
		Point size = mapControl.getSize();
		if (x > 0 && y > 0 && w <= size.x && h <= size.y) {
			mapControl.redraw(x, y, w, h, false);
		}
	}
	
	public void cancelRendering() {
		if (rhandler != null) {
			if (!rhandler.isStopped()) {
				rhandler.cancel();
				mapControl.redraw();
			}
		}
	}
		
	
	public void renderBuffer() {
		cancelRendering();
		rhandler = new EditorRenderHandler();
		renderService.render(rhandler);
	}

	public void drag(int shiftX, int shiftY) {
		scene.shift.x = shiftX;
		scene.shift.y = shiftY;
		redraw();
	}

	public void move(int dx, int dy) {
		
		double rate = scene.getRate();
		
		double mx = - dx / rate;
		double my = dy / rate;

		Bounds viewport = scene.getViewport();
		viewport.translate(mx, my);
		scene.revalidate();
		refreshBufferAndRedrawControl();
	}

	public ZoomStrategy getZoomStrategy() {
		return zoomStrategy;
	}

	public void setZoomStrategy(ZoomStrategy zoomStrategy) {
		this.zoomStrategy = zoomStrategy;
	}
}
