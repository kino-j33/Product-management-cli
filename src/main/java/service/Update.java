package service;

import static util.PrintUtil.*;
import static util.ScannerUtil.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import data.ConnectionManager;
import data.DataConflictValidator;
import data.DataSearchManager;
import dto.Item;
import exception.DataConflictException;
import exception.ErrorHandler;
import exception.UserInterruptException;
import util.ChoiceType;
import util.Validator;

/**
 * @author Yamashita
 */
public class Update extends AbstractServiceConfirmation {

	private Connection connection = null;
	private String newProductCode = null;

	/**
	 * 商品変更機能のメイン処理
	 * @author Yamashita
	 * @throws DataConflictException 
	 */
	@Override
	public void process() {
		try {
			if (connection == null || connection.isClosed()) {
				this.connection = ConnectionManager.getConnection();
			}

			while (true) {
				println("商品情報を変更します。");
				//商品IDの検索
				Item item = findProductById();

				//商品更新の処理
				executeUpdateProduct(item);

				// 続けて変更しますか？
				ChoiceType userContinueChoice = chooseNextAction("変更");
				if (userContinueChoice == ChoiceType.NO) {
					break;
				}
			}

		} catch (UserInterruptException e) {// exit・\qでメニューに戻る
			;
		} catch (DataConflictException e) {// 排他処理
			println(e.getMessage());
		} catch (SQLException e) {
			ErrorHandler.handleConnectionException(e);
			e.printStackTrace();
		} finally {
			ConnectionManager.close(connection);
		}
		println("メニューに戻ります。");
	}

	//=========================================
	/**
	 * 商品ID検索と情報取得
	 * @return Item
	 * @throws UserInterruptException exitの例外
	 * @author Yamashita
	 */
	//=========================================
	private Item findProductById() throws UserInterruptException {
		final String ERR_MESSAGE = "存在しない商品IDです。商品IDを確認してください。";

		println("変更する商品IDを入力してください。");
		while (true) {
			print("商品ID > ");
			String inputId = getInput();

			// 商品IDの入力チェック
			if (!(Validator.isItemId(inputId) && Validator.isItemId10(inputId))) {
				println(ERR_MESSAGE);
				continue;
			}

			Item item = DataSearchManager.findById(inputId);
			if (item == null) {
				println(ERR_MESSAGE);
				continue;
			}

			return item;
		}
	}

	//=========================================
	/**
	 * 更新する情報の入力を行うメソッド
	 * @param item
	 * @throws DataConflictException  排他処理の例外
	 * @throws UserInterruptException exitの例外
	 * @author Yamashita
	 */
	//=========================================
	private void executeUpdateProduct(Item item)
			throws DataConflictException, UserInterruptException {

		// 後学のためにメモ：テキストブロックでテンプレを作っても、改行はJDBC側で無視されるので問題ない
		final String SQL = """
				UPDATE product SET
				  code=?
				  ,name=?
				  ,category=?
				  ,sale_price=?
				  ,purchase_price=?
				  ,registration_date=?
				  ,version_no=?
				  ,record_update_timestamp=?
				WHERE sid=?
				""";

		try (PreparedStatement pstmt = connection.prepareStatement(SQL);) {
			printSeparator();

			// ====== 入力処理 ======
			prepareUpdateStatement(pstmt, item);

			printSeparator();

			// ====== 実行確認 ======
			ChoiceType userUpdateChoice = confirmExecution("変更");
			if (userUpdateChoice == ChoiceType.NO) {
				return;
			}

			// ====== DB更新処理 ====== 
			try {
				//楽観的排他処理
				DataConflictValidator.checkConflictById(item, "変更");

				// コードの重複チェック
				if (!(productCodeDuplicateCheck(newProductCode, item.getSid()))) {
					throw new DataConflictException("この商品コードはすでに使用されています。新しい商品コードを設定してください。");
				}

				connection.setAutoCommit(false);
				int updateCount = pstmt.executeUpdate();

				//データ異常を検知した場合
				if (updateCount != 1) {
					throw new SQLException();
				}

				connection.commit();
				enableAutoCommit();
				println("商品情報を変更しました。");

				//変更結果の商品情報取得
				showUpdatedInfo(item.getId());

			} catch (SQLException e) {
				try {
					connection.rollback();
					ErrorHandler.handleSQLException(e);

				} catch (SQLException e1) {
					e1.printStackTrace();
					ErrorHandler.handleSQLException(e1);
				}

			} finally {
				enableAutoCommit();
			}

		} catch (SQLException e) {
			ErrorHandler.handleSQLException(e);
		}
	}

	//=========================================
	/**
	 * 変更する項目の入力を行うメソッド
	 * @param pstmt
	 * @param item
	 * @return
	 * @throws SQLException
	 * @throws UserInterruptException
	 */
	//=========================================
	private void prepareUpdateStatement(PreparedStatement pstmt, Item item)
			throws SQLException, UserInterruptException {
		println("変更する項目のみ入力してください。");
		printSeparator();

		newProductCode = changeCode(item);
		pstmt.setString(1, newProductCode);
		pstmt.setString(2, changeNameAndCategory("商品名", item.getName()));
		pstmt.setString(3, changeNameAndCategory("商品分類", item.getCategory()));

		Integer salePrice = changeSalePrice(item);
		if (salePrice != null) {
			pstmt.setInt(4, salePrice);
		} else {
			pstmt.setNull(4, java.sql.Types.INTEGER);
		}

		Integer purchasePrice = changePurchasePrice(item);
		if (purchasePrice != null) {
			pstmt.setInt(5, purchasePrice);
		} else {
			pstmt.setNull(5, java.sql.Types.INTEGER);
		}

		Date registrationDate = changeRegistrationDate(item);
		if (registrationDate != null) {
			pstmt.setDate(6, registrationDate);
		} else {
			pstmt.setNull(6, java.sql.Types.DATE);
		}

		pstmt.setInt(7, item.getVersionNo() + 1); // バージョン番号のセット
		// レコード更新日時のセット
		pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
		pstmt.setInt(9, item.getSid()); // SIDのセット
	}

	/*=========================================
	 *  変更する項目の入力とチェックメソッドの呼び出し
	 *=========================================*/
	/**
	 * 商品コード項目の入力
	 * @param item
	 * @return
	 * @throws UserInterruptException
	 * @throws SQLException
	 */
	//=========================================
	private String changeCode(Item item)
			throws UserInterruptException, SQLException {
		final String ERR_MESSAGE = "商品コードは13桁の半角数字で入力してください。";

		while (true) {
			print("商品コード[" + item.getCode() + "] > ");
			String inputString = getInput();

			// enterが入力されたらスキップ
			if (emptyInputCheck(inputString)) {
				return item.getCode();
			}

			// 商品コードの入力チェック
			if (!Validator.isNumeric13(inputString)) {
				println(ERR_MESSAGE);
				continue;
			}

			// 商品コードの重複チェック
			if (!(productCodeDuplicateCheck(inputString, item.getSid()))) {
				println("この商品コードはすでに使用されています。新しい商品コードを設定してください。");
				continue;
			}

			return inputString;
		}
	}

	//=========================================
	/**
	 * 商品名項目の入力
	 * @param item
	 * @return
	 * @throws UserInterruptException
	 */
	//=========================================
	private String changeNameAndCategory(String label, String currentValue)
			throws UserInterruptException {
		final String ERR_MESSAGE = label + "は100文字以下で入力してください。";

		while (true) {
			print(label + "[" + currentValue + "] > ");
			String inputString = getInput();

			if (emptyInputCheck(inputString)) {
				return currentValue;
			}

			if (!Validator.isNameOk(inputString)) {
				println(ERR_MESSAGE);
				continue;
			}
			return inputString;
		}
	}

	//	//=========================================
	//	/**
	//	 * 商品名項目の入力
	//	 * @param item
	//	 * @return
	//	 * @throws UserInterruptException
	//	 */
	//	//=========================================
	//	private String changeName(Item item)
	//			throws UserInterruptException {
	//		final String ERR_MESSAGE = "商品名は100文字以下で入力してください。";
	//
	//		while (true) {
	//			print("商品名[" + item.getName() + "] > ");
	//			String inputString = getInput();
	//
	//			// enterが入力されたらスキップ
	//			if (emptyInputCheck(inputString)) {
	//				return item.getName();
	//			}
	//
	//			// 商品コードの入力チェック
	//			if (!Validator.isNameOk(inputString)) {
	//				println(ERR_MESSAGE);
	//				continue;
	//			}
	//			return inputString;
	//		}
	//	}

	//=========================================
	/**
	 * 商品分類項目の入力
	 * @param item
	 * @return
	 * @throws UserInterruptException
	 * @author Yamashita
	 */
	//=========================================
	//	private String changeCategory(Item item)
	//			throws UserInterruptException {
	//		final String ERR_MESSAGE = "商品分類は100文字以下で入力してください。";
	//
	//		while (true) {
	//			print("商品分類[" + item.getCategory() + "] > ");
	//			String inputString = getInput();
	//
	//			// enterが入力されたらスキップ
	//			if (emptyInputCheck(inputString)) {
	//				return item.getCategory();
	//			}
	//
	//			//商品分類の入力チェック
	//			if (!Validator.isNameOk(inputString)) {
	//				println(ERR_MESSAGE);
	//				continue;
	//			}
	//			return inputString;
	//		}
	//	}

	//=========================================
	/**
	 * 販売単価項目の入力
	 * @param item
	 * @return
	 * @throws UserInterruptException
	 * @author Yamashita
	 */
	//=========================================
	private Integer changeSalePrice(Item item)
			throws UserInterruptException {
		final String ERR_MESSAGE = "販売単価は半角数字8桁以下で入力してください。";

		while (true) {
			print("販売単価[" + item.getSalePrice() + "] > ");
			String inputString = getInput();

			// enterが入力されたらスキップ
			if (emptyInputCheck(inputString)) {
				if (item.getSalePrice() == null) {
					return null;
				}
				return item.getSalePrice();
			}

			if (inputString.equals("null")) {
				return null;
			}

			// 価格表記のチェック
			if (!Validator.isNumeric8(inputString)) {
				println(ERR_MESSAGE);
				continue;
			}

			return Integer.parseInt(inputString);
		}
	}

	//=========================================
	/**
	 * 仕入単価項目の入力
	 * @param item
	 * @return
	 * @throws UserInterruptException
	 * @author Yamashita
	 */
	//=========================================
	private Integer changePurchasePrice(Item item)
			throws UserInterruptException {
		final String ERR_MESSAGE = "仕入単価は半角数字8桁以下で入力してください。";

		while (true) {
			print("仕入単価[" + item.getPurchasePrice() + "] > ");
			String inputString = getInput();

			// enterが入力されたらスキップ
			if (emptyInputCheck(inputString)) {
				if (item.getPurchasePrice() == null) {
					return null;
				}
				return item.getPurchasePrice();
			}

			if (inputString.equals("null")) {
				return null;
			}

			// 価格表記のチェック
			if (!Validator.isNumeric8(inputString)) {
				println(ERR_MESSAGE);
				continue;
			}

			return Integer.parseInt(inputString);
		}
	}

	//=========================================
	/**
	 * 登録日項目の入力
	 * @param item
	 * @return
	 * @throws UserInterruptException
	 * @author Yamashita
	 */
	//=========================================
	private Date changeRegistrationDate(Item item)
			throws UserInterruptException {
		final String ERR_MESSAGE = "登録日はyyyy-MM-dd形式で入力してください。";
		while (true) {
			print("登録日[" + item.getRegistrationDate() + "] > ");
			String inputString = getInput();

			if (emptyInputCheck(inputString)) { //enterが入力されたらスキップ
				if (item.getRegistrationDate() == null) {
					return null;
				}
				return Date.valueOf(item.getRegistrationDate());
			}

			if (inputString.equals("null")) {
				return null;
			}

			// yyyy-MM-dd形式でのチェック
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			try {
				LocalDate localDate = LocalDate.parse(inputString, formatter);
				return Date.valueOf(localDate);
			} catch (DateTimeParseException e) {
				println(ERR_MESSAGE);
				continue;
			}
		}
	}

	/*=========================================
	 *  入力チェック
	 *=========================================*/

	//=========================================
	/**
	 * 商品コードの重複チェック
	 * @param targetCode 検索対象のコード
	 * @param targetSid 検索対象のSID
	 * @return 重複なし：true、重複あり：false
	 * @throws SQLException
	 * @author Yamashita
	 */
	//=========================================
	private boolean productCodeDuplicateCheck(
			String targetCode, int targetSid)
			throws SQLException {

		Item newItem = DataSearchManager.findByCode(targetCode);

		if (newItem == null) {
			return true;
		}

		if (newItem.getSid().equals(targetSid)) {
			return true;
		}
		return false;
	}

	//=========================================
	/**
	 * 変更時にenterで入力をスキップするか確認
	 * @param inputString
	 * @return 入力なし：true、入力あり：false
	 */
	//=========================================
	private boolean emptyInputCheck(String inputString) {
		return inputString.isEmpty();
	}

	//=========================================
	/**
	 * 商品情報変更後の表示を行うメソッド
	 * @param targetId 商品ID
	 */
	//=========================================
	private void showUpdatedInfo(String targetId) {
		Item updatedItem = DataSearchManager.findById(targetId);

		if (updatedItem != null) {
			printResultSeparator();

			println("商品ID = " + updatedItem.getId());
			println("商品コード = " + updatedItem.getCode());
			println("商品名 = " + updatedItem.getName());
			println("商品分類 = " + updatedItem.getCategory());
			println("販売単価 = " + convertNullToEmpty(updatedItem.getSalePrice()));
			println("仕入単価 = " + convertNullToEmpty(updatedItem.getPurchasePrice()));
			println("登録日 = " + convertNullToEmpty(updatedItem.getRegistrationDate()));
		}
	}

	//=========================================
	/**
	 * 商品情報を表示がnullの場合、空文字を返す
	 * @param <T>
	 * @param target 確認対象
	 * @return
	 * @author Yamashita
	 */
	//=========================================
	private <T> String convertNullToEmpty(T target) {
		return target == null ? "" : target.toString();
	}

	/**
	 * connection.setAutoCommitをtrueにするメソッド
	 * @author Yamashita
	 */
	private void enableAutoCommit() {
		try {
			if (connection != null
					&& !connection.isClosed()
					&& !connection.getAutoCommit()) {
				connection.setAutoCommit(true);
			}
		} catch (SQLException e) {
			ErrorHandler.handleSQLException(e);
		}
	}
}