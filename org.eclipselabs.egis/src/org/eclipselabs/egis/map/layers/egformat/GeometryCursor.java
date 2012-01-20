package org.eclipselabs.egis.map.layers.egformat;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.eclipselabs.egis.core.EGisException;
import org.eclipselabs.egis.geometry.Geometry;
import org.eclipselabs.egis.geometry.Line;

import sqlitewrapper.Statement;

public class GeometryCursor {

	private final Statement stmt;
	
	public GeometryCursor(Statement stmt) {
		this.stmt = stmt;
	}

	public boolean next() {
		return stmt.next();
	}
	
	public Object getRaw() {
		return stmt.columnBlob(0);
	}

	public Geometry getGeometry()  {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(stmt.columnBlob(1)));
		try {
			return Line.fromBinary(in);
		} catch (IOException e) {
			throw new EGisException(e);
		}
	}
	
	public void close() {
		stmt.close();
	}
}
