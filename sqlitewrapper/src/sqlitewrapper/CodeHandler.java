package sqlitewrapper;

public class CodeHandler {

	public static int checkForError(int code) throws DatabaseException {
		
		if (code != Code.SQLITE_OK.code &&
			code != Code.SQLITE_ROW.code &&
			code != Code.SQLITE_DONE.code) {
			throw new DatabaseException(code);
		}
		return code;
	}

}
