package org.eclipselabs.egis.render.tiles;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.graphics.Image;
import org.eclipselabs.egis.core.Controller;
import org.eclipselabs.egis.geometry.Bounds;

public class TileService implements Tile.Handler {

	private final Controller controller;
	private final Map<String, Tile> tiles = new ConcurrentHashMap<String, Tile>();
	private final ExecutorService executorService = Executors.newFixedThreadPool(15);
	
	public TileService(Controller controller) {
		this.controller = controller;
	}
	
	public Image getTile(String key, Bounds bounds) {
		Tile tile = tiles.get(key);
		if (tile == null) {
			String location = "http://tile.openstreetmap.org/"+key+".png";
			tile = new Tile(location, this, executorService, bounds);
			tiles.put(key, tile);
			tile.load();
		}
		return tile.getImage();
	}
	
	@Override
	public void onLoad(Tile tile) {
		controller.redraw(tile.getBounds());
	}
}
