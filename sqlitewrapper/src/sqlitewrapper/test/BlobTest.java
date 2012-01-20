package sqlitewrapper.test;

import java.util.Arrays;

import sqlitewrapper.Database;
import sqlitewrapper.Statement;

public class BlobTest {

	public static void main(String[] args) {
		
		String fileName = "/home/xibyte/downloads/sqlite-amalgamation-3070900/test.blob";
		Database db = Database.open(fileName, 0);
		db.exec("DROP TABLE IF EXISTS blb");
		db.exec("CREATE TABLE blb(data blob)");
		
		db.exec("BEGIN");
		
		Statement stmt = db.prepare("INSERT INTO blb VALUES(?)");
		
		byte[] data = new byte[5];
		for (int i = 0; i < data.length;++i) {
			data[i] = (byte) i;
		}
		stmt.bindBlob(1, data);
		stmt.step();
		stmt.close();
		db.exec("COMMIT");
		
		stmt = db.prepare("SELECT data FROM blb");
		while (stmt.next()) {
			System.out.println(Arrays.toString(stmt.columnBlob(0))); 
		}
		stmt.close();
		db.close();
	}
	
	
}
