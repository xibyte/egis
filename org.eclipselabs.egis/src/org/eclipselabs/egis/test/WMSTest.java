package org.eclipselabs.egis.test;

import org.eclipselabs.egis.tiles.WMSService;

public class WMSTest {

	public static void main(String[] args) {
		WMSService wmsService = new WMSService();
		System.out.println(wmsService.toWMSURL(10, 10, 5, 256));
	}
	
}
