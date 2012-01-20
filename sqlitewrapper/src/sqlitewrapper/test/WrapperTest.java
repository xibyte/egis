package sqlitewrapper.test;

import sqlitewrapper.Database;
import sqlitewrapper.DatabaseException;
import sqlitewrapper.Statement;

public class WrapperTest {

	public static void main(String[] args) throws DatabaseException {
		String fileName = "/home/xibyte/downloads/sqlite-amalgamation-3070900/test";
		Database db = Database.open(fileName, 0);
		db.exec("DROP TABLE IF EXISTS geo_idx");
		db.exec("CREATE VIRTUAL TABLE geo_idx USING rtree(id, x1, x2, y1, y2)");
		
		db.exec("BEGIN");
		
		Statement stmt = db.prepare("INSERT INTO geo_idx values(?, ?, ?, ?, ?)");
		
		int id = 0;
		for (int i = 0; i< 1000; ++i) {
			for (int j = 0; j< 1000; ++j) {
				int x = i*10;
				int y = j*10;
				addData(stmt, ++id, x, y, x+5, y+5);
			}
		}
		stmt.close();
		db.exec("COMMIT");
		
		stmt = db.prepare("SELECT id FROM geo_idx WHERE x1>=5 AND x2<=25 AND y1>=5 AND y2<=25");
		stmt.step();
		System.out.println(stmt.columnInt(0));
		stmt.close();
		db.close();
	}

	private static void addData(Statement stmt, int id, double x1, double y1,
			double x2, double y2) throws DatabaseException {
		stmt.bindInt(1, id);
		stmt.bindDouble(2, x1);
		stmt.bindDouble(3, x2);
		stmt.bindDouble(4, y1);
		stmt.bindDouble(5, y2);
		stmt.step();
		stmt.resetAndClearBindings();
	}
	
}
