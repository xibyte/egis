package sqlitewrapper;

import static sqlitewrapper.CodeHandler.checkForError;

public class Statement {

	private long pointer; 
	
	Statement(long pointer) {
		this.pointer = pointer;
	}
	
	public boolean next() throws DatabaseException {
		return step() != Code.SQLITE_DONE.code; 
	}
	
	public int step() throws DatabaseException {
		return checkForError(stepInternal(pointer));
	}

	public int reset() throws DatabaseException {
		return checkForError(resetInternal(pointer));
	}

	public int clearBindings() throws DatabaseException {
		return checkForError(clearBindingsInternal(pointer));
	}

	public int resetAndClearBindings() throws DatabaseException {
		return checkForError(resetAndClearBindingsInternal(pointer));
	}
	public int dataCount() {
		return dataCountInternal(pointer);
	}
	
	public int columnCount() {
		return columnCountInternal(pointer);
	};
	
	public int columnInt(int column) {
		return columnIntInternal(pointer, column);
	};
	
	public double columnDouble(int column) {
		return columnDoubleInternal(pointer, column);
	}
	
	public byte[] columnBlob(int column) {
		return columnBlobInternal(pointer, column);
	}
	
	public void bindInt(int column, int value) throws DatabaseException {
		checkForError(bindIntInternal(pointer, column, value));
	}

	public void bindDouble(int column, double value) throws DatabaseException {
		checkForError(bindDoubleInternal(pointer, column, value));
	}

	public void bindBlob(int column, byte[] value) throws DatabaseException {
		checkForError(bindBlobInternal(pointer, column, value, value.length));
	}
	
	public void close() throws DatabaseException {
		try {
			checkForError(closeInternal(pointer));
		} finally {
			pointer = 0;
		}
	}

	native static void prepareInternal(long[] result, long db, String sql);
	
	native int stepInternal(long pointer);  
	
	native int resetInternal(long pointer);
	
	native int clearBindingsInternal(long pointer);
	
	native int resetAndClearBindingsInternal(long pointer);

	native int dataCountInternal(long pointer);
	
	native int columnCountInternal(long pointer);
	
	native int columnIntInternal(long pointer, int column);
	
	native double columnDoubleInternal(long pointer, int column);
	
	native byte[] columnBlobInternal(long pointer, int column);
	
	native int bindIntInternal(long pointer, int column, int value);
	
	native int bindDoubleInternal(long pointer, int column, double value);

	native int bindBlobInternal(long pointer, int column, byte[] value, int size);
	
	native int closeInternal(long pointer);
}
