package service;

import static util.PrintUtil.*;

import java.util.List;

import data.DataSearchManager;
import dto.Item;
import exception.UserInterruptException;
import util.ChoiceType;
import util.PrintUtil;
import util.ScannerUtil;

public class Search extends AbstractService {
	/**
	* 検索のメインクラス
	* @author 　member
	*/
	@Override
	public void process() {
		try {
			while (true) {
				String keyword = null;
				println("商品情報を検索します。");//画面表示
				println("検索キーワードを入力してください。");//画面表示
				PrintUtil.printSeparator();
				print("キーワード > ");

				keyword = ScannerUtil.getInput();

				// 検索実行　DataSearchManagerからキーワードに合致するItemを入力
				//keywordに文字列で渡して、商品検索
				//Listはデータの集まりをItem型の商品情報を返してもらう
				//返ってきたリストをitemsに入れる

				List<Item> items = DataSearchManager.findByKeyword(keyword);
				try {
					if (items.isEmpty()) {
						println("検索結果は0件です。");
					} else {
						println("検索結果は " + items.size() + " 件です。");
						printResultSeparator();
						for (Item item : items) {
							printElements(item);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					println("データベース接続に失敗しました。");
					return; // メニューに戻る
				}

				// 続けて検索するか確認
				ChoiceType userChoice = chooseNextAction("検索");
				if (userChoice == ChoiceType.NO) {
					break;
				}
			}
		} catch (UserInterruptException e) {
			// ユーザーが exit や ￥q を入力した場合の処理
			//println("検索を中断しました。");
			//return;
		}
	}
}
