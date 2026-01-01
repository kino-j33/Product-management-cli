package service;

import static util.PrintUtil.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import data.ConnectionManager;
import data.DataSearchManager;
import dto.Item;
import exception.ErrorHandler;
import exception.UserInterruptException;
import util.ChoiceType;
import util.ScannerUtil;
import util.Validator;

/**
 * Register　商品登録クラス
 * @author member
 */

public class Register extends AbstractServiceConfirmation {
	Connection connection = null;

	/**
	 * 商品登録のメイン処理
	 * @author member
	 */
	@Override
	public void process() {

		try {
			this.connection = ConnectionManager.getConnection();
			while (true) {
				printMenuSeparator();
				println("商品情報を登録します。");
				println("商品情報を入力してください。");
				printSeparator();

				//入力データをDTO変換
				Item item = new Item();

				item.setId(inputProductId());
				item.setCode(inputProductCode());
				item.setName(inputProductName());
				item.setCategory(inputProductCategory());
				item.setSalePrice(inputSalePrice());
				item.setPurchasePrice(inputPurchasePrice());
				item.setRegistrationDate(inputRegistrationDate());
				item.setDeleted("0"); //削除フラグ　1が削除済
				item.setVersionNo(1); //ver.1で登録
				item.setRecordCreationTimestamp(LocalDateTime.now());
				item.setRecordUpdateTimestamp(LocalDateTime.now());

				// 登録確認
				printSeparator();
				ChoiceType userChoice = confirmExecution("登録");
				if (userChoice == ChoiceType.YES) {

					//データ更新直前の重複チェック
					//id重複
					if (DataSearchManager.findById(item.getId()) != null) {
						println("この商品IDはすでに使用されています。新しい商品IDを設定してください。");
						break;
					}
					//コード重複
					if (DataSearchManager.findByCode(item.getCode()) != null) {
						println("この商品コードはすでに使用されています。新しい商品コードを設定してください。");
						break;
					}

					//登録実行
					registerProduct(item);

					println("商品情報を登録しました。");
					printResultSeparator();
					println("商品ID = " + item.getId());
					println("商品コード = " + item.getCode());
					println("商品名 = " + item.getName());
					println("商品分類 = " + item.getCategory());
					println("販売単価 = " + item.getSalePrice());
					println("仕入単価 = " + item.getPurchasePrice());
					println("登録日 = " + item.getRegistrationDate());
				}

				// 続行確認
				ChoiceType userContinueChoice = chooseNextAction("登録");
				if (userContinueChoice == ChoiceType.NO) {
					break;
				}
			}
		} catch (UserInterruptException e) {

		} catch (SQLException e) {
			ErrorHandler.handleSQLException(e);
		} finally {
			ConnectionManager.close(connection);
		}
		println("メニューに戻ります。");
	}

	//-----------------------------------------------------------------
	//関連（private）メソッド↓

	/**
	 * 商品ID入力処理
	 * @author member
	 * @throws UserInterruptException 
	 */
	private String inputProductId() throws UserInterruptException {
		while (true) {
			print("商品ID > ");
			String id = ScannerUtil.getInput();

			//共通　nullチェック
			if (Validator.isNull(id)) {
				println("nullは使用できません。");
				continue;
			}

			// ①商品ID　半角英数字
			if (!Validator.isItemId(id)) {
				println("商品IDは半角英数字・半角アンダースコア・半角ハイフンで入力してください。");
				continue;
			}

			// ②商品ID　10桁
			if (!Validator.isItemId10(id)) {
				println("商品IDは１０桁で入力してください。");
				continue;
			}

			// ③商品ID　重複チェック
			if (DataSearchManager.findById(id) != null) {
				println("この商品IDはすでに使用されています。新しい商品IDを設定してください。");
				continue;
			}
			return id;
		}
	}

	/**
	 * 商品コード入力処理
	 * @author member
	 * @throws UserInterruptException 
	 */
	private String inputProductCode() throws UserInterruptException {
		while (true) {
			print("商品コード > ");
			String code = ScannerUtil.getInput();

			//共通　nullチェック
			if (Validator.isNull(code)) {
				println("nullは使用できません。");
				continue;
			}

			//④コード　数字13桁
			if (!Validator.isNumeric13(code)) {
				println("商品コードは13桁の半角数字で入力してください。");
				continue;
			}

			// ⑤コード重複チェック
			if (DataSearchManager.findByCode(code) != null) {
				println("この商品コードはすでに使用されています。新しい商品コードを設定してください。");
				continue;
			}

			return code;
		}
	}

	/**
	 * 商品名入力処理
	 * @author member
	 * @throws UserInterruptException 
	 */
	private String inputProductName() throws UserInterruptException {
		while (true) {
			print("商品名 > ");
			String name = ScannerUtil.getInput();

			//共通　nullチェック
			if (Validator.isNull(name)) {
				println("nullは使用できません。");
				continue;
			}

			//⑥商品名　入力必須
			if (!Validator.isItemName(name)) {
				println("商品名を入力してください。");
				continue;
			}

			//⑦商品名　100文字以下
			if (!Validator.isNameOk(name)) {
				println("商品名は１００文字以下で入力してください。");
				continue;
			}

			return name;
		}
	}

	/**
	 * 商品分類入力処理
	 * @author member
	 * @throws UserInterruptException 
	 */
	private String inputProductCategory() throws UserInterruptException {
		while (true) {
			print("商品分類 > ");
			String category = ScannerUtil.getInput();

			//共通　nullチェック
			if (Validator.isNull(category)) {
				println("nullは使用できません。");
				continue;
			}

			//⑧商品分類　入力必須
			if (!Validator.isItemName(category)) {
				println("商品分類を入力してください。");
				continue;
			}

			//⑨商品分類　100文字以下
			if (!Validator.isNameOk(category)) {
				println("商品分類は１００文字以下で入力してください。");
				continue;
			}

			return category;
		}
	}

	/**
	 * 販売単価入力処理
	 * @author member
	 * @throws UserInterruptException 
	 */
	private Integer inputSalePrice() throws UserInterruptException {
		while (true) {
			print("販売単価 > ");
			String salePrice = ScannerUtil.getInput();

			//空白はnullにする
			if (salePrice.length() == 0) {
				return null;
			}

			//⑩販売単価　半角数字8桁以下
			if (!Validator.isNumeric8(salePrice)) {
				println("販売単価は半角数字8桁以下で入力してください。");
				continue;
			}

			// 数値（int）に変換
			int price = Integer.parseInt(salePrice);
			return price;
		}
	}

	/**
	 * 仕入単価入力処理
	 * @author member
	 * @throws UserInterruptException 
	 */
	private Integer inputPurchasePrice() throws UserInterruptException {
		while (true) {
			print("仕入単価 > ");
			String purchasePrice = ScannerUtil.getInput();

			//空文字はnullにする
			if (purchasePrice.length() == 0) {
				return null;
			}

			//⑪仕入単価　半角数字8桁以下
			if (!Validator.isNumeric8(purchasePrice)) {
				println("仕入単価は半角数字8桁以下で入力してください。");
				continue;
			}

			// 数値（int）に変換
			int price = Integer.parseInt(purchasePrice);
			return price;
		}
	}

	/**
	 * 登録日入力処理
	 * @author member
	 * @throws UserInterruptException 
	 */

	private LocalDate inputRegistrationDate() throws UserInterruptException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		while (true) {
			print("登録日 > ");
			String registrationDate = ScannerUtil.getInput();

			//⑫登録日　10桁　有効日付
			//TODO Varidationクラス化
			if (!Validator.isInputOk(registrationDate, 10)) {
				println("登録日はyyyy-MM-dd形式で入力してください。");
				continue;
			}

			try {
				LocalDate localDate = LocalDate.parse(registrationDate, formatter);
				return localDate;
			} catch (DateTimeParseException e) {
				println("有効な日付ではありません。正しい日付で入力してください。");
				continue;
			}
		}
	}

	/**
	 * 登録実行処理
	 * @author member
	 * @throws SQLException 
	 */
	private void registerProduct(Item item) throws SQLException {
		String sql = "INSERT INTO product " +
				"(id, code, name, category, sale_price, purchase_price, registration_date, deleted, version_no, record_creation_timestamp, record_update_timestamp) "
				+
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		connection.setAutoCommit(false);
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, item.getId());
			ps.setString(2, item.getCode());
			ps.setString(3, item.getName());
			ps.setString(4, item.getCategory());
			ps.setInt(5, item.getSalePrice());
			ps.setInt(6, item.getPurchasePrice());
			ps.setDate(7, Date.valueOf(item.getRegistrationDate()));
			ps.setString(8, item.getDeleted());
			ps.setInt(9, item.getVersionNo());
			ps.setTimestamp(10, Timestamp.valueOf(item.getRecordCreationTimestamp()));
			ps.setTimestamp(11, Timestamp.valueOf(item.getRecordUpdateTimestamp()));

			int result = ps.executeUpdate();
			if (result > 0) {
				connection.commit();
			} else {
				connection.rollback();
				throw new SQLException();
			}

		} finally {
			connection.setAutoCommit(true);
		}
	}

}//クラスブロック