package exception;

/**
 * 楽観的排他処理で重複となった場合にthrowされる例外です
 * @author Yamashita
 */
public class DataConflictException extends Exception {

	/**
	 * @author Yamashita
	 */
	public DataConflictException() {
		super();
	}

	/**
	 * @author Yamashita
	 */
	public DataConflictException(String message) {
		super(message);
	}

	/**
	 * @author Yamashita
	 */
	public DataConflictException(String message, Throwable t) {
		super(message, t);
	}

	/**
	 * @author Yamashita
	 */
	public DataConflictException(Throwable t) {
		super(t);
	}
}
