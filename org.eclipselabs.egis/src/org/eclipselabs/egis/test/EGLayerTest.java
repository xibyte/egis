package org.eclipselabs.egis.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipselabs.egis.geometry.Line;
import org.eclipselabs.egis.map.layers.egformat.EGLayer;
import org.eclipselabs.egis.map.layers.egformat.GeometryCollector;
import org.eclipselabs.egis.map.layers.egformat.GeometryCursor;
import org.geotools.data.FeatureReader;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import sqlitewrapper.Database;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class EGLayerTest {

	
	public static void main(String[] args) throws Exception {
		read();
	}

	
	private static void read() {
		EGLayer layer = openLayer();
		GeometryCursor cursor = layer.queryGeometry(-0.5017, 0.4808, -0.4939, 0.4837);
		while (cursor.next()) {
			System.out.println(cursor.getGeometry());
		}
		cursor.close();
	}	
	
	@SuppressWarnings({"unused", "deprecation"})
	private static void write() throws MalformedURLException, IOException {
		EGLayer layer = openLayer();
		layer.initIfNeed();
		layer.clear();
		
		
		ShapefileDataStore dstore = new ShapefileDataStore(new File("/home/xibyte/trash/egis/house.shp").toURL());;
		FeatureReader<SimpleFeatureType, SimpleFeature> reader = dstore.getFeatureReader();
		
		layer.beginTransaction();
		GeometryCollector collector = layer.collect();
		int id = 0;
		while (reader.hasNext()) {
			System.out.println("Processing " + id);
			SimpleFeature feature = reader.next();
			com.vividsolutions.jts.geom.Geometry geom = (Geometry) feature.getDefaultGeometry();
			
			com.vividsolutions.jts.geom.Polygon poly = null;
			if (geom instanceof com.vividsolutions.jts.geom.Polygon) {
				poly = (com.vividsolutions.jts.geom.Polygon) geom;
			} else 	if (geom instanceof com.vividsolutions.jts.geom.MultiPolygon) {
				poly = (Polygon) geom.getGeometryN(0); 
			}
			if (poly != null) {
			
				System.out.println(poly.toString());
				Coordinate[] coordinates = poly.getExteriorRing().getCoordinates();
				Line line = new Line(coordinates.length);
				for (int i = 0; i < coordinates.length; i++) {
					Coordinate coor = coordinates[i];
					line.xPoints[i] = coor.x;
					line.yPoints[i] = coor.y;
				}
				collector.add(line, ++id);
			}
		}
		layer.commit();
		collector.close();
		layer.dispose();
	}



	private static EGLayer openLayer() {
		Database db = Database.open("/home/xibyte/trash/egis/house.eg", 0);
		EGLayer layer = new EGLayer(db, "houses");
		return layer;
	}
	
}
