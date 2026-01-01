package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.Item;

/**
 *商品のItem オブジェクトを生成しリターンするクラス。
 * @author member
 */

public class DataSearchManager {
	/**
	 * 商品IDで検索を行い削除されていなければ
	 * toItemメソッドに引き渡し生成されたItemのオブジェクトをreturnするメソッドです。
	 * @param String searchId 検索したい商品のIDを引数にしてください
	 * @return ResultSet の内容から生成された Item オブジェクト
	 * @throws Exception
	 * @author member
	 */
	private static final String GET_ID_SQL = "SELECT * FROM public.product WHERE id = ? AND deleted = '0' ";
	private static final String GET_CODE_SQL = "SELECT * FROM public.product WHERE code = ? AND deleted = '0' ";
	// SQL 文を定義　いずれかの条件に該当させる
	private static final String GET_KEYWORD = "SELECT * FROM public.product ";
	private static final String SELECT_ACTIVE = "deleted = '0' ORDER BY id ASC";

	public static Item findById(String searchId) {
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(GET_ID_SQL);) {
			pstmt.setString(1, searchId);
			ResultSet resultSet = pstmt.executeQuery();

			if (resultSet.next()) {
				return toItem(resultSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * コードの検索を行い結果をItemで返す
	 * ヒットしなければnullを返す
	 * @author member
	 */
	public static Item findByCode(String searchCode) {
		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(GET_CODE_SQL);) {
			pstmt.setString(1, searchCode);
			ResultSet resultSet = pstmt.executeQuery();

			if (resultSet.next()) {
				return toItem(resultSet);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Id,Code,Nameでの検索を行うためのSQLを作成し実行、itemを返す
	 * @author member
	 */

	public static List<Item> findByKeyword(String keyword) {
		List<Item> items = new ArrayList<>();

		boolean isEmpty = keyword.isEmpty();
		StringBuffer sql = new StringBuffer();
		sql.append(GET_KEYWORD);

		if (isEmpty) {
			sql.append("WHERE ");
		} else {
			sql.append("WHERE (id LIKE ? OR code LIKE ? OR name LIKE ?) AND ");
		}
		sql.append(SELECT_ACTIVE);

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

			// ？のプレースホルダ（3）あるため、部分一致用キーワードをセット
			if (!isEmpty) {
				for (int i = 1; i <= 3; i++) {
					pstmt.setString(i, "%" + keyword + "%");
				}
			}

			// ResultSetからItemを安全に作り出す
			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				items.add(toItem(resultSet));
			}
			return items;
			//sqlで起きる全般的なエラーがでたとき表示させる
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * findByIdから渡されたResultSetの内容からItemオブジェクトを生成します。
	 * @param String searchId 検索したい商品のIDを引数にしてください
	 * @return ResultSet の内容から生成された Item オブジェクト
	 * @throws Exception
	 * @author member
	 */
	public static Item toItem(ResultSet resultSet) {
		try {
			Item item = new Item(
					resultSet.getInt("sid"),
					resultSet.getString("id"),
					resultSet.getString("code"),
					resultSet.getString("name"),
					resultSet.getString("category"),
					resultSet.getInt("sale_price"),
					resultSet.getInt("purchase_price"),
					resultSet.getDate("registration_date") != null
							? resultSet.getDate("registration_date").toLocalDate()
							: null,
					resultSet.getString("deleted"),
					resultSet.getInt("version_no"),
					resultSet.getTimestamp("record_creation_timestamp") != null
							? resultSet.getTimestamp("record_creation_timestamp").toLocalDateTime()
							: null,
					resultSet.getTimestamp("record_update_timestamp") != null
							? resultSet.getTimestamp("record_update_timestamp").toLocalDateTime()
							: null);
			return item;

		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}