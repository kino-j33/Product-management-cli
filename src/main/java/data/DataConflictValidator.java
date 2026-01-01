package data;

import java.sql.Connection;
import java.sql.SQLException;

import dto.Item;
import exception.DataConflictException;
import exception.ErrorHandler;

/**
 * 楽観的排他処理のクラスです
 * 「商品管理システム_v2_1_仕様書（追加仕様書）_v1.pdf」の
 * 「2.4.4 排他処理仕様」
 * 「2.5.3 排他処理仕様」
 * 「3.4.4 同時更新エラー」に基づいて変更と削除の排他処理を行います
 *  @author Yamashita
 */
public class DataConflictValidator {
	/**
	 * IDでの検索を行い、バージョン番号での比較を行います
	 * バージョン番号に相違がある場合はDataConflictExceptionをthrowします
	 * メソッド呼び出し元ではcatchブロック内で下記の記載と、メニューに戻る処理をお願いします
	 * System.out.println(e.getMessage());
	 * 
	 * @param oldItem 機能（変更/削除）のItemを引数にしてください
	 * @param operationLabel 変更/削除のいずれかの文字列を引数にしてください
	 * @throws DataConflictException
	 * @author Yamashita
	 */
	public static void checkConflictById(Item oldItem, String operationLabel) throws DataConflictException {
		String validatorTargetId = oldItem.getId();
		Integer validatorTargetVersionNo = oldItem.getVersionNo();

		try (Connection connection = ConnectionManager.getConnection();) {
			Item newItem = DataSearchManager.findById(validatorTargetId);

			if (newItem == null
					|| !validatorTargetVersionNo.equals(newItem.getVersionNo())) {
				StringBuffer sb = new StringBuffer();
				sb.append("更新に失敗しました。他のユーザーがデータを変更したため、処理を完了できませんでした。")
						.append(operationLabel)
						.append("処理の最初から操作をやり直してください。");
				throw new DataConflictException(sb.toString());
			}
		} catch (SQLException e) {
			ErrorHandler.handleSQLException(e);
		}
	}
}
