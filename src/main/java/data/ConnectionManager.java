package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 *DB接続管理のクラス
 * @author member
 */

public class ConnectionManager {
	/**
	 *DB接続メソッド
	 * 例）serviceパッケージの削除メソッドの場合下記のように使用します
	 * try (Connection conn = ConnectionManager.getConnection();) {
	 *～～～
	 *～～～
	 *PreparedStatement updateStmt1 = conn
	 *						.prepareStatement("UPDATE product SET deleted = ? WHERE id = ?");
	 *}
	 *
	 * @return データベースへの接続オブジェクトを返します。
	 *		   接続に失敗した場合はnullを返します。
	 * @throws IllegalStateException JDBCドライバーのロードに失敗した場合
	 * @author member
	 */
	public static Connection getConnection() {
		try {
			DBdto dbConfig = DBconfig.loadConfig();
			try {
				Class.forName(dbConfig.getDriver());
			} catch (ClassNotFoundException e1) {
				throw new IllegalStateException("ドライバーのロードに失敗しました");
			}

			Connection conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(),
					dbConfig.getPassword());
			return conn;
		} catch (Exception e) {
			System.out.println("データソースに接続できません。システム管理者に連絡してください。");
		}
		return null;
	}

	/**
	 *DB接続を閉じるメソッド
	 * @param クローズするデータベース接続オブジェクト
	 * @author member
	 */

	public static void close(Connection connectionn) {
		if (connectionn != null)
			try {
				connectionn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 *ResultSetを閉じるメソッド
	 * @param resultSet クローズする ResultSet オブジェクト
	 * @author member
	 */
	public static void ResultSetclose(ResultSet resultSet) {
		if (resultSet != null)
			try {

			} catch (Exception e) {
				// TODO: handle exception
			}

	}
}
