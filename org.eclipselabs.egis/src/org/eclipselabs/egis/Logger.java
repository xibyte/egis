package org.eclipselabs.egis;

import org.eclipse.core.runtime.Status;

public class Logger {

	public static void log(Throwable th) {
		EGisPlugin
				.getDefault()
				.getLog()
				.log(new Status(Status.ERROR, EGisPlugin.PLUGIN_ID, th
						.getMessage(), th));
	}

}
