package org.eclipselabs.egis.render.tiles;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipselabs.egis.core.OSM;
import org.eclipselabs.egis.core.Scene;
import org.eclipselabs.egis.geometry.Bounds;
import org.eclipselabs.egis.geometry.Point;
import org.eclipselabs.egis.render.Superposition;

/**
 * see http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
 */
public class TilesPaintListener implements PaintListener {

	private boolean active;
	private final Scene scene;
	private final TileService tileService;
	
	public TilesPaintListener(Scene scene, TileService tileService) {
		this.scene = scene;
		this.tileService = tileService;
	}

	public boolean isActive() {
		return active;
	}

	@Override
	public void paintControl(PaintEvent e) {

		//Implementation of OSM TMS. Just for example
		//This code should be generalize.
		
//		if (!active) {
//			return;
//		}

		//Lets consider that we in Mercator SR

		Bounds viewport = scene.getViewport().copy();
		double dx = scene.scaleScreen(scene.shift.x);
		double dy = scene.scaleScreen(scene.shift.y);
		
		{ //Applying restrictions 
		
			viewport.translate(dx, dy);
		
			double rx = scene.scaleScreen(e.x);
			double ry = scene.scaleScreen(e.y);
			double rw = scene.scaleScreen(e.width);
			double rh = scene.scaleScreen(e.height);
			
			viewport.x1 += rx;
			viewport.y1 += ry;
			viewport.setWidth(rw);
			viewport.setHeight(rh);
		}
		
		int level = OSM.getLevel(scene);
		
		Point start = OSM.getTileXY(viewport.x1, viewport.y1, level);
		Point end = OSM.getTileXY(viewport.x2, viewport.y2, level);
		
		OSM.getTileXY(7370931.93, 9230529.73, level);
		
		int vw = scene.getViewWidth();
		int vh = scene.getViewHeight();
		
		int startX = (int) start.x;
		int startY = (int) start.y;
		int endX = (int) end.x;
		int endY = (int) end.y;
		
		int posX = (int) (start.x - startX) * OSM.TILE_SIZE + e.x; 
		int posY = (int) (start.y - startY) * OSM.TILE_SIZE + e.y; 
		
		
		double worldTileSize = scene.scaleScreen(OSM.TILE_SIZE);
		
		Bounds tileBounds = viewport.copy();
		tileBounds.setWidth(worldTileSize); 
		tileBounds.setHeight(worldTileSize); 
		
		for (int i = startX; i < endX; ++i) {
			for (int j = startY; j < endY; ++j) {
				String key = level+"/"+i +"/"+j;

				Image tile = tileService.getTile(key, tileBounds.copy());
				
				if (tile != null) {
					Superposition sp = Superposition.make(posX, posY, vw, vh,
							OSM.TILE_SIZE, OSM.TILE_SIZE, e.x, e.y, e.width,
							e.height);
					
					if (sp.isPositive()) {
						e.gc.drawImage(tile, 
								sp.srcX, sp.srcY, 
								sp.width, sp.height, 
								sp.dstX, sp.dstY, 
								sp.width, sp.height);
					}
				}
				posX += OSM.TILE_SIZE;
				posY += OSM.TILE_SIZE;
				tileBounds.translate(0, worldTileSize);
			}
			tileBounds.translate(worldTileSize, 0);
		}
	}
	
}
