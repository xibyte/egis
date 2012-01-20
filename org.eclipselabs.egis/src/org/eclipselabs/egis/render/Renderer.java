package org.eclipselabs.egis.render;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipselabs.egis.core.Scene;
import org.eclipselabs.egis.geometry.Bounds;
import org.eclipselabs.egis.geometry.Geometry;
import org.eclipselabs.egis.map.Layer;
import org.eclipselabs.egis.map.layers.egformat.GeometryCursor;

public class Renderer {

	public static final Color BACKGROUND = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	
	public void render(Scene scene, GC gc, Handler handler) {
		handler.begin();
		try {
			int width = scene.getViewWidth();
			int height = scene.getViewHeight();
			gc.setClipping(0, 0, width, height);
			gc.setBackground(BACKGROUND);
			gc.fillRectangle(0, 0, width, height);
			
			Geometry.Visitor renderVisitor = new RenderVisitor(scene, gc);
			Bounds bounds = scene.getViewport();	
			List<Layer> layers = scene.getMap().getLayers();
			for (Layer layer : layers) {
				if (!needToRendred(layer)) {
					continue;
				}
				try {
					GeometryCursor geometries = layer.queryGeometry(bounds.x1,
							bounds.y1, bounds.x2, bounds.y2);
					try {
						while (geometries.next()) {
							Geometry geom = null;
							try {
								geom = geometries.getGeometry();
								geom.accept(renderVisitor);
								handler.geometryRendered(geom);
							} catch (Exception e) {
								handler.geometryProblem(geom, e);
							}
							if (handler.isCanceled()) {
								return;
							}
						}
					} finally {
						geometries.close();
						handler.layerFinished(layer);					
					}
				} catch (Exception e) {
					handler.layerProblem(layer, e);
				}
			}
		} finally {
			handler.end();
		}
	}

	private boolean needToRendred(Layer layer) {
		return true;
	}

	public static interface Handler {
		
		void begin();

		void end();

		void layerFinished(Layer layer);
		
		void layerProblem(Layer layer, Exception e);

		void geometryRendered(Geometry geom);

		void geometryProblem(Geometry geom, Exception e);

		boolean isCanceled();
	}
	
}
