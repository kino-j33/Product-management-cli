package service;

import static util.PrintUtil.*;
import static util.ScannerUtil.*;

import exception.UserInterruptException;
import util.ChoiceType;

/**
 * @author Yamashita
 */
public abstract class AbstractService implements ServiceProcessable {
	/**
	 * 「続けて〇〇しますか？1:続けて検索する 2:メニューへ戻る > 1」の選択を行うメソッドです
	 * 
	 * 例）「続けて変更しますか？」の場合下記のように使用します
	 * ChoiceType userChoice = chooseNextAction("変更");
	 * if (userChoice == ChoiceType.NO) {
	 *   break;
	 * }
	 * 
	 * @param operationLabel 検索・登録・変更・削除のいずれかの文字列を引数にしてください
	 * @return enum型のChoiceTypeが戻り値になります
	 * @throws UserInterruptException exitの例外
	 * @author Yamashita
	 */
	ChoiceType chooseNextAction(String operationLabel) throws UserInterruptException {
		printResultSeparator();
		while (true) {
			println("続けて商品を" + operationLabel + "しますか？");
			print("1:続けて" + operationLabel + "する 2:メニューへ戻る > ");

			String nextAction = getInput();

			ChoiceType choiceType = parseNumberChoice(nextAction);

			if (choiceType == null) {
				println("入力値が間違っています。正しい値を入力してください。");
				continue;
			}

			return choiceType;
		}
	}

	/**
	 * 入力された文字とChoiceTypeのNumberを比較
	 * 
	 * @param input 
	 * @return inputと一致したChoiceType
	 * @author Yamashita
	 */
	private ChoiceType parseNumberChoice(String input) {
		for (ChoiceType ct : ChoiceType.values()) {
			if (input.equals(ct.getNumber())) {
				return ct;
			}
		}
		return null;
	}
}
