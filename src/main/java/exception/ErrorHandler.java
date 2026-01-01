package exception;

import java.sql.SQLException;

/**
* @author Yamashita
*/

public class ErrorHandler {
	/**
	* SQL例外の処理
	* @param e SQL例外
	* @author Yamashita
	*/
	public static void handleSQLException(SQLException e) {
		System.err.println("処理を実行できませんでした。システム管理者に連絡してください。");
		e.printStackTrace();
	}

	/**
	* データベース接続エラーの処理
	* @param e SQL例外
	* @author Yamashita
	*/
	public static void handleConnectionException(SQLException e) {
		System.err.println("データソースに接続できません。システム管理者に連絡してください。");
		e.printStackTrace();
	}

	/**
	* システム例外の処理
	* @param e 例外
	* @author Yamashita
	*/
	public static void handleSystemException(Exception e) {
		System.err.println("システムエラーが発生しました。システム管理者に連絡してください。");
		e.printStackTrace();
	}
}
