package org.eclipselabs.egis.ui;


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipselabs.egis.core.Controller;
import org.eclipselabs.egis.core.OSM;
import org.eclipselabs.egis.core.OSMZoomStrategy;
import org.eclipselabs.egis.core.Scene;
import org.eclipselabs.egis.core.instruments.InstrumentsManager;
import org.eclipselabs.egis.core.instruments.standart.NavigationInstrument;
import org.eclipselabs.egis.geometry.Bounds;
import org.eclipselabs.egis.map.Map;
import org.eclipselabs.egis.map.layers.egformat.EGLayer;
import org.eclipselabs.egis.render.BufferedRenderService;
import org.eclipselabs.egis.render.MapPaintListener;
import org.eclipselabs.egis.render.Renderer;
import org.eclipselabs.egis.render.tiles.TileService;
import org.eclipselabs.egis.render.tiles.TilesPaintListener;

import sqlitewrapper.Database;

public class MapEditor extends EditorPart {

	private BufferedRenderService renderService;
	private Map map;

	public MapEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	private static EGLayer openLayer() {
		Database db = Database.open("/home/xibyte/trash/egis/house.eg", 0);
		EGLayer layer = new EGLayer(db, "houses");
		return layer;
	}

	private Scene scene;
	private InstrumentsManager instrumentsManager;
	private Controller controller;
	

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		
		final Canvas canvas = new Canvas(parent, SWT.NO_REDRAW_RESIZE
				| SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND); // SWT.DOUBLE_BUFFERED ???
		
		
		ControlListener initialListener = new ControlAdapter() {
			
			@Override
			public void controlResized(ControlEvent e) {

				Point size = canvas.getSize();
				
				map = new Map();
				map.getLayers().add(openLayer());
				scene = new Scene(map);
				Bounds bounds = new Bounds(-0.4817, 0.4608, -0.5139, 0.5037);
				scene.setSizeAndViewTo(size.x, size.y, bounds);
				
				//OSM
//				scene.setSize(size.x, size.y);
//				OSM.set(scene, 1, 0, 0);
				
				TileService tileService = new TileService(controller);
				TilesPaintListener tilesPaintListener = new TilesPaintListener(scene, tileService);
				
				renderService = new BufferedRenderService(scene, new Renderer(), size.x, size.y);
				
				MapPaintListener mapPaintListener = new MapPaintListener(renderService, scene);
				instrumentsManager = new InstrumentsManager(canvas);
				controller = new Controller(scene, canvas, renderService);
				//OSM
//				controller.setZoomStrategy(new OSMZoomStrategy());
				controller.renderBuffer();
				
				instrumentsManager.set(new NavigationInstrument(controller));

				//Combine to own listener
				canvas.addPaintListener(mapPaintListener);
				canvas.addPaintListener(tilesPaintListener);
				
				canvas.removeControlListener(this);
				canvas.setBackground(Renderer.BACKGROUND);
				
			}
		};
		canvas.addControlListener(initialListener);
		canvas.addControlListener(new ControlAdapter() {
			
			@Override
			public void controlResized(ControlEvent e) {
				controller.adjustSize();
			}
		});
	}
	
	@Override
	public void setFocus() {

	}

	@Override
	public void dispose() {
		if (renderService != null) {
			renderService.dispose();
		}
		if (map != null) {
			map.dispose();
		}
		super.dispose();
	}
	
}
