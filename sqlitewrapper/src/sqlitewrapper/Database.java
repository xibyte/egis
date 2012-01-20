package sqlitewrapper;

import static sqlitewrapper.CodeHandler.checkForError;

public class Database {
	
	private long pointer;
	
	Database(long pointer) {
		this.pointer = pointer; 
	}
	
	static {
		System.loadLibrary("sqlitewrapper");
	}
	
	public static Database open(String file, int mode) throws DatabaseException {
		long[] result = new long[2]; 
		openInternal(result, file, mode);
		checkForError((int) result[0]);
		Database database = new Database(result[1]);
		return database;
	}

	public Statement prepare(String sql) throws DatabaseException {
		long[] result = new long[2];
		Statement.prepareInternal(result, pointer, sql);
		checkForError((int) result[0]);
		return new Statement(result[1]);
	}

	public void close() throws DatabaseException {
		try {
			checkForError(closeInternal(pointer));
		} finally {
			pointer = 0;
		}
	}	
	
	public void exec(String sql) throws DatabaseException {
		checkForError(execInternal(pointer, sql));
	}

	native static void openInternal(long[] result, String file, int mode);
	
	native int execInternal(long pointer, String sql);
	
	native int closeInternal(long pointer);
}
