package org.eclipselabs.egis.map.layers.egformat;


import org.eclipselabs.egis.Logger;
import org.eclipselabs.egis.map.Layer;

import sqlitewrapper.Database;
import sqlitewrapper.Statement;

public class EGLayer implements Layer {

	private final Database database;
	private final String layerId;
	
	public EGLayer(Database database, String layerId) {
		this.database = database;
		this.layerId = layerId;
	}

	public void initIfNeed() {
		database.exec("CREATE TABLE IF NOT EXISTS "+getTable()+"(id LONG, location BLOB)");
		database.exec("CREATE VIRTUAL TABLE "+getIndexTable()+" USING rtree(id, x1, x2, y1, y2)");
	}

	public void revalidateIndex() {
		
	}
	
	public IdCursor queryId(double x1, double y1, double x2, double y2) {
		return null;
	}
	
	@Override
	public GeometryCursor queryGeometry(double x1, double y1, double x2, double y2) {
		
		Statement stmt = database.prepare("SELECT id, location FROM "
				+ getTable() + " WHERE id in (SELECT id FROM "
				+ getIndexTable()
				+ " WHERE x2>=? AND x1<=? AND y2>=? AND y1<=?)");
		try {
			stmt.bindDouble(1, x1);
			stmt.bindDouble(2, x2);
			stmt.bindDouble(3, y1);
			stmt.bindDouble(4, y2);
		} catch (RuntimeException e) {
			try {
				stmt.close();
			} catch (RuntimeException ec) {
				Logger.log(ec);
			}
			throw e;
		}
		return new GeometryCursor(stmt);
	}

	public FeatureCursor query(double x1, double y1, double x2, double y2) {
		return null;
	}
	
	public GeometryCollector collect() {
		Statement stmt = database.prepare("INSERT INTO "+getTable()+"(id, location) values(?, ?)");
		Statement idxStmt = database.prepare("INSERT INTO "+getIndexTable()+" values(?, ?, ?, ?, ?)");
		return new GeometryCollector(stmt, idxStmt);
	}

	public void clear() {
		database.exec("DELETE FROM "+getTable());
		database.exec("DELETE FROM "+getIndexTable());
	}
	
	public String getIndexTable() {
		return layerId + "_idx";
	}

	public String getTable() {
		return layerId;
	}
	
	public void beginTransaction() {
		database.exec("BEGIN");
	}

	public void commit() {
		database.exec("COMMIT");
	}

	public void rollback() {
		database.exec("ROLLBACK");
	}
	
	@Override
	public void dispose() { 
		database.close();
	}
	
}
