package org.eclipselabs.egis.core.instruments.standart;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Control;
import org.eclipselabs.egis.core.Controller;
import org.eclipselabs.egis.core.instruments.Instrument;

public class NavigationInstrument implements Instrument, MouseListener,
		MouseMoveListener, MouseWheelListener, KeyListener {

	private final Controller ctrl;
	private boolean dragging;
	private int lastX;
	private int lastY;
	private int dx;
	private int dy;
	
	public NavigationInstrument(Controller ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public void activate(Control mapControl) {
		mapControl.addMouseListener(this);
		mapControl.addMouseMoveListener(this);
		mapControl.addMouseWheelListener(this);
		mapControl.addKeyListener(this);
	}

	@Override
	public void deActivate(Control mapControl) {
		mapControl.removeMouseListener(this);
		mapControl.removeMouseMoveListener(this);
		mapControl.removeMouseWheelListener(this);
		mapControl.removeKeyListener(this);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {

	}

	@Override
	public void mouseDown(MouseEvent e) {
		dragging = true;
		lastX = e.x;
		lastY = e.y;
		dx = dy = 0;
	}

	@Override
	public void mouseUp(MouseEvent e) {
		dragging = false;
		ctrl.drag(0, 0);
		if (dx != 0 && dy != 0) {
			ctrl.move(dx, dy);
		}
		dx = dy = 0;
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if (dragging) {
			dx += e.x - lastX;
			dy += e.y - lastY;
			if (dx != 0 && dy != 0) {
				lastX = e.x;
				lastY = e.y;
				ctrl.drag(dx, dy);
			}
		}
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		if (e.count > 0) {
			ctrl.zoomIn(e.x, e.y);
		} else {
			ctrl.zoomOut(e.x, e.y);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
