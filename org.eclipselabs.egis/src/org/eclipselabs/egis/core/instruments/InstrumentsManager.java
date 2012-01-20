package org.eclipselabs.egis.core.instruments;

import static org.eclipselabs.egis.core.instruments.Instrument.NULL;

import org.eclipse.swt.widgets.Control;

public class InstrumentsManager {

	private final Control mapControl;
	private Instrument current = NULL;
	
	public InstrumentsManager(Control mapControl) {
		this.mapControl = mapControl;
	}
	
	public void set(Instrument instrument) {
		current.deActivate(mapControl);
		current = instrument;
		current.activate(mapControl);
	}
}
