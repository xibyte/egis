package org.eclipselabs.egis.render.tiles;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.eclipse.swt.graphics.Image;
import org.eclipselabs.egis.geometry.Bounds;

public class Tile implements Runnable {

	private final String location;
	private final Handler handler;
	private volatile Image image;
	private final ExecutorService executorService;
	private Future<?> future;
	private final Bounds bounds;
	
	public Tile(String location, Handler handler,
			ExecutorService executorService, Bounds bounds) {
		this.location = location;
		this.handler = handler;
		this.executorService = executorService;
		this.bounds = bounds;
	}

	public void load() {
		future = executorService.submit(this);
	}
	
	public String getLocation() {
		return location;
	}

	@Override
	public void run() {
		InputStream stream;
		try {
			stream = new URL(location).openStream();
			try {
				image = new Image(null, stream);
				handler.onLoad(this);
			} finally {
				stream.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public static interface Handler {
		
		void onLoad(Tile sender);
		
	}
	
	public void dispose() {
		if (future != null) {
			future.cancel(true);
		}
		if (image != null) {
			image.dispose();
		}
	}

	public Bounds getBounds() {
		return bounds;
	}

	public Image getImage() {
		return image;
	}
}
