package org.eclipselabs.egis.map.layers.egformat;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.eclipselabs.egis.core.EGisException;
import org.eclipselabs.egis.geometry.Bounds;
import org.eclipselabs.egis.geometry.Geometry;

import sqlitewrapper.Statement;

public class GeometryCollector {

	private final Statement stmt;
	private final Statement idxStmt;
	private final ByteArrayOutputStream bos = new ByteArrayOutputStream(256);
	private final DataOutputStream out = new DataOutputStream(bos);
	
	public GeometryCollector(Statement stmt, Statement idxStmt) {
		this.stmt = stmt;
		this.idxStmt = idxStmt;
	}

	public void add(Geometry geometry, int id) {
		bos.reset();
		try {
			geometry.toBinary(out);
			stmt.bindInt(1, id);
			stmt.bindBlob(2, bos.toByteArray());
			stmt.step();
			stmt.resetAndClearBindings();
			
			Bounds bounds = new Bounds();
			geometry.expandBounds(bounds);
			
			idxStmt.bindInt(1, id);
			idxStmt.bindDouble(2, bounds.x1);
			idxStmt.bindDouble(3, bounds.x2);
			idxStmt.bindDouble(4, bounds.y1);
			idxStmt.bindDouble(5, bounds.y2);
			idxStmt.step();
			idxStmt.resetAndClearBindings();
			
		} catch (Exception e) {
			throw new EGisException(e);
		}
	}
	
	public void close() {
		stmt.close();
		idxStmt.close();
	}
}
