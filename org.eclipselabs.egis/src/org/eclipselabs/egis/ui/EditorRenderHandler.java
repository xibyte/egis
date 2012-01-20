package org.eclipselabs.egis.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipselabs.egis.Logger;
import org.eclipselabs.egis.geometry.Geometry;
import org.eclipselabs.egis.map.Layer;
import org.eclipselabs.egis.render.Renderer;

public class EditorRenderHandler implements Renderer.Handler{

	private boolean stopped;
	private int total;
	
	public void cancel() {
		stopped = true;
	}
	
	@Override
	public void begin() {
		total = 0;
		stopped = false;
	}

	@Override
	public void end() {
//		System.out.println("Total shapes rendered: "+total);
		stopped = true;
	}

	@Override
	public void layerFinished(Layer layer) {
		
	}

	@Override
	public void layerProblem(Layer layer, Exception e) {
		Logger.log(e);
	}

	@Override
	public void geometryProblem(Geometry geom, Exception e) {
		Logger.log(e);
	}

	@Override
	public boolean isCanceled() {
		return stopped;
	}

	@Override
	public void geometryRendered(Geometry geom) {
		total++;
		if (total % 500 == 0) {
			Display.getCurrent().readAndDispatch();
		}
	}

	public boolean isStopped() {
		return stopped;
	}
}
