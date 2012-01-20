package org.eclipselabs.egis.render;

import org.eclipse.swt.graphics.GC;
import org.eclipselabs.egis.core.Scene;
import org.eclipselabs.egis.geometry.Geometry;
import org.eclipselabs.egis.geometry.Line;

public class RenderVisitor implements Geometry.Visitor {
	
	private final Scene scene;
	private final GC gc;
	
	public RenderVisitor(Scene scene, GC gc) {
		this.scene = scene;
		this.gc = gc;
	}

	@Override
	public void visitLine(Line line) {
		
		int length = line.xPoints.length;
		int[] points = new int[length * 2];
		
		for (int i = 0, j = 0; i < length; i++, j += 2) {
			points[j] = scene.mapToScreenX(line.xPoints[i]);
			points[j+1] = scene.mapToScreenY(line.yPoints[i]);
		}
		gc.drawPolyline(points);
		
//		SHOW BOUNDS		
//		Bounds bounds = new Bounds();
//		line.expandBounds(bounds);
//		gc.drawRectangle(scene.mapToScreenX(bounds.x1),
//				scene.mapToScreenY(bounds.y1),
//				scene.mapToScreenX(bounds.x2) - scene.mapToScreenX(bounds.x1),
//				scene.mapToScreenY(bounds.y2) - scene.mapToScreenY(bounds.y1));
	}
}
