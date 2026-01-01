package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import data.ConnectionManager;
import data.DataConflictValidator;
import data.DataSearchManager;
import dto.Item;
import exception.DataConflictException;
import exception.UserInterruptException;
import util.ChoiceType;
import util.PrintUtil;
import util.ScannerUtil;

/**
 * 商品削除クラス
 * @author member
 */
public class Delete extends AbstractServiceConfirmation {
	private static final String label = "削除";

	/**
	 * 商品削除メソッド
	 * @author　member
	 */
	public void process() {
		try {
			while (true) {
				PrintUtil.printSeparator();
				PrintUtil.println("★商品情報を削除します。");
				String searchId = null;
				while (true) {
					PrintUtil.print("★削除する商品IDを入力してください。>");
					searchId = ScannerUtil.getInput();
					PrintUtil.println("商品ID >" + searchId);

					try (Connection conn = ConnectionManager.getConnection();) {
						Item item = DataSearchManager.findById(searchId);
						if (item == null) {
							PrintUtil.println("この商品IDのデータは存在しません");
							PrintUtil.printSeparator();
							continue;
						}

						PrintUtil.printSeparator();
						String id = item.getId();
						PrintUtil.println("商品ID：" + id);

						String code = item.getCode();
						PrintUtil.println("商品コード：" + code);

						String name = item.getName();
						PrintUtil.println("商品名：" + name);

						String category = item.getCategory();
						PrintUtil.println("商品分類：" + category);

						int sale_price = item.getSalePrice();
						PrintUtil.println("販売単価：" + sale_price);

						int purchase_price = item.getPurchasePrice();
						PrintUtil.println("仕入単価：" + purchase_price);

						LocalDate registration_date = item.getRegistrationDate();
						PrintUtil.println("登録日：" + registration_date);

						String deleted = item.getDeleted();
						PrintUtil.println("削除フラグ：" + deleted);

						int version_no = item.getVersionNo();
						PrintUtil.println("バージョン番号：" + version_no);

						LocalDate record_creation_timestamp = item.getRegistrationDate();
						PrintUtil.println("レコード作成日時：" + record_creation_timestamp);

						LocalDateTime record_update_timestamp = item.getRecordUpdateTimestamp();
						PrintUtil.println("レコード更新日時：" + record_update_timestamp);
						PrintUtil.printSeparator();

						do {
							ChoiceType userChoice1 = confirmExecution(label);
							DataConflictValidator.checkConflictById(item, label);//排他処理

							if (userChoice1 == ChoiceType.YES) {
								int rs2 = 0;
								int deleted1 = Integer.valueOf(deleted);
								if (deleted1 == 0) {
									PreparedStatement updateStmt1 = conn
											.prepareStatement("UPDATE product SET deleted = ? WHERE id = ?");
									deleted1++;
									updateStmt1.setInt(1, deleted1);
									updateStmt1.setString(2, searchId);
									rs2 = updateStmt1.executeUpdate();

									PreparedStatement updateStmt2 = conn
											.prepareStatement("UPDATE product SET version_no = ? WHERE id = ?");
									version_no++;
									updateStmt2.setInt(1, version_no);
									updateStmt2.setString(2, searchId);
									updateStmt2.executeUpdate();
								} else if (version_no == 1) {
									PrintUtil.println("削除済みです。");
								}

								if (rs2 > 0) {
									PrintUtil.printSeparator();
									PrintUtil.println("商品ID：" + id);
									PrintUtil.println("JANコード：" + code);
									PrintUtil.println("商品名：" + name);
									PrintUtil.println("商品分類：" + category);
									PrintUtil.println("販売単価：" + sale_price);
									PrintUtil.println("仕入単価：" + purchase_price);
									PrintUtil.println("登録日：" + registration_date);
									PrintUtil.println("削除フラグ：" + deleted1);
									PrintUtil.println("バージョン番号：" + version_no);
									PrintUtil.println("レコード作成日時：" + record_creation_timestamp);
									PrintUtil.println("レコード更新日時：" + record_update_timestamp);
									PrintUtil.println("★商品情報を削除しました。");
									PrintUtil.printSeparator();

									ChoiceType userChoice2 = chooseNextAction(label);
									if (userChoice2 == ChoiceType.YES) {
										break;
									} else if (userChoice2 == ChoiceType.NO) {
										PrintUtil.println("メニューに戻ります。");
										PrintUtil.printMenuSeparator();
										return;
									}
								}
								break;

							} else if (userChoice1 == ChoiceType.NO) {
								PrintUtil.printSeparator();
								ChoiceType userChoice2 = chooseNextAction(label);
								if (userChoice2 == ChoiceType.YES) {
									break;
								} else if (userChoice2 == ChoiceType.NO) {
									PrintUtil.println("メニューに戻ります。");
									PrintUtil.printMenuSeparator();
									return;
								}
							} else {
								PrintUtil.printSeparator();
								PrintUtil.println("YまたはNを入力してください。");
							}
						} while (true);
					}
				}
			}
		} catch (SQLException e) {
			PrintUtil.println("エラーが発生しました。メニューに戻ります。");

			return;
		} catch (DataConflictException e) {
			PrintUtil.println(e.getMessage());//排他処理メッセージ
			return;
		} catch (UserInterruptException e) {
			PrintUtil.println("メニューに戻ります");

		}
	}
}