package org.eclipselabs.egis.core.instruments;

import org.eclipse.swt.widgets.Control;

public interface Instrument {
	
	void activate(Control mapControl);
	
	void deActivate(Control mapControl);

	public static final Instrument NULL = new Instrument() {
		
		@Override
		public void deActivate(Control mapControl) {
		}
		
		@Override
		public void activate(Control mapControl) {
		}
	};
}
