package sqlitewrapper;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public final int code;

	public DatabaseException(int code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		Code ecode = Code.byNumberCode(code);
		String msg = "sqlite error '" + code + "'";
		if (ecode != null) {
			msg += ". '" + ecode.description + "'";
		}
		return msg;
	}
}