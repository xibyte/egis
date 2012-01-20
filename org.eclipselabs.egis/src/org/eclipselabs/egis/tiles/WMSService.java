/*
 * WMSService.java
 *
 * Created on October 7, 2006, 6:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.eclipselabs.egis.tiles;


/**
 * A class that represents a WMS mapping service. 
 * See http://en.wikipedia.org/wiki/Web_Map_Service for more information.
 * @author joshy
 */
public class WMSService {
    private String baseUrl;
    private String layer;
    /** Creates a new instance of WMSService */
    public WMSService() {
        // by default use a known nasa server
        setLayer("BMNG");
        setBaseUrl("http://wms.jpl.nasa.gov/wms.cgi?");
    }
    public WMSService(String baseUrl, String layer) {
        this.baseUrl = baseUrl;
        this.layer = layer;
    }
    
    public String toWMSURL(int x, int y, int zoom, int tileSize) {
        String format = "image/jpeg";
        String styles = "";
        String srs = "EPSG:4326";
        int ts = tileSize;
        int circumference = widthOfWorldInPixels(zoom, tileSize);
        double radius = circumference / (2* Math.PI);
        double ulx = MercatorUtils.xToLong(x*ts, radius);
        double uly = MercatorUtils.yToLat(y*ts, radius);
        double lrx = MercatorUtils.xToLong((x+1)*ts, radius);
        double lry = MercatorUtils.yToLat((y+1)*ts, radius);
        String bbox = ulx + "," + uly+","+lrx+","+lry;
        String url = getBaseUrl() + 
                "version=1.1.1&request="+
                "GetMap&Layers="+layer+
                "&format="+format+
                "&BBOX="+bbox+
                "&width="+ts+"&height="+ts+
                "&SRS="+srs+
                "&Styles="+styles+
                //"&transparent=TRUE"+
                "";
        return url;
    }
    
    
    private int widthOfWorldInPixels(int zoom, int TILE_SIZE) {
//        int TILE_SIZE = 256;
        int tiles = (int)Math.pow(2 , zoom);
        int circumference = TILE_SIZE * tiles;
        return circumference;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
   
}
