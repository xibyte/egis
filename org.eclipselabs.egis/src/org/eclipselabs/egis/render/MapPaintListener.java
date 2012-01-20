package org.eclipselabs.egis.render;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipselabs.egis.core.Scene;

public class MapPaintListener implements PaintListener {

	private final BufferedRenderService renderService;
	private final Scene scene;
	
	public MapPaintListener(BufferedRenderService renderService, Scene scene) {
		this.renderService = renderService;
		this.scene = scene;
	}

	@Override
	public void paintControl(PaintEvent e) {
		
		int w = scene.getViewWidth();
		int h = scene.getViewHeight();
		
		Superposition sp = Superposition.make(scene.shift.x, scene.shift.y, 
				w, h, w, h, e.x, e.y, e.width, e.height);
		
//		rasterService.paint(scene, e.gc, srcX, srcY, adaptedWidth, adaptedHeight);
		
		if (sp.isPositive()) {
			Image buffImage = renderService.getBufferedImage();
			e.gc.drawImage(buffImage, 
					sp.srcX, sp.srcY, 
					sp.width, sp.height, 
					sp.dstX, sp.dstY, 
					sp.width, sp.height);
//			e.gc.drawImage(renderService.getBufferedImage(), 
//					-shiftX, -shiftY, 
//					adaptedWidth, adaptedHeight, 
//					shiftX, shiftX, 
//					adaptedWidth, adaptedHeight);
		}
		
		if (1 == 1) {
			return;
		}
		
		//Ниже пример как нарисовать дырку
		
		Device device = e.gc.getDevice();
		Image image = new Image(device, "/home/xibyte/snapshot.jpg");
		e.gc.drawImage(image, 0, 0);
		
		// paint polygon with hole
		int[] points = new int[]{10, 10, 200, 10, 200, 200, 10, 200};
		int[] hole = new int[]{30, 30, 170, 30, 170, 170, 30, 170};
		
		
		Image holeBackup = new Image(device, 140, 140);
		e.gc.copyArea(holeBackup, 30, 30);
		
		e.gc.setBackground(device.getSystemColor(SWT.COLOR_WHITE));
		e.gc.setForeground(device.getSystemColor(SWT.COLOR_CYAN));
		e.gc.setLineWidth(5);
		
		e.gc.fillPolygon(points);
		e.gc.drawPolygon(points);

		e.gc.drawImage(holeBackup, 30, 30);
		e.gc.drawPolygon(hole);
		
//		e.gc.fillPolygon(points);
	}

}
