package selector;

import static util.PrintUtil.*;

import exception.UserInterruptException;
import util.ScannerUtil;

/**
 * @author Yamashita
 */
public class MainMenu {
	/**
	 * メインメニューの表示と各機能のインスタンス化・メソッドの実行
	* @author Yamashita
	*/
	public void menu() {
		// TODO 「商品管理メニューVol5」実機確認
		println("商品管理メニューVol5");

		try {
			while (true) {
				printMenuSeparator();
				displayMenu();

				String menuUserSelection = ScannerUtil.getInput();
				menuUserSelection = menuUserSelection.trim();
				MainMenuEnum selectService = null;

				for (MainMenuEnum menu : MainMenuEnum.values()) {
					if (menuUserSelection.equals(menu.getMenuNumber())) {
						selectService = menu;
					}
				}

				if (selectService == null) {
					println("メニュー番号が間違っています。正しい値を入力してください。");
					continue;
				}

				selectService.getServiceProcessable().process();

				if (selectService == MainMenuEnum.EXIT) {
					break;
				}
			}

			// exit・\qでの終了
		} catch (UserInterruptException e) {
			MainMenuEnum.EXIT.getServiceProcessable().process();
		}
	}

	/**
	 * メニュー項目の表示
	 * @author Yamashita
	 */
	private void displayMenu() {
		StringBuffer sb = new StringBuffer();
		sb.append("[メニュー] ");
		for (MainMenuEnum menu : MainMenuEnum.values()) {
			sb.append(menu).append(" ");
		}
		sb.append("> ");
		print(sb.toString());
	}
}