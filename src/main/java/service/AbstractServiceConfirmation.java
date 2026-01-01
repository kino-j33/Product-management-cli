package service;

import static util.PrintUtil.*;
import static util.ScannerUtil.*;

import exception.UserInterruptException;
import util.ChoiceType;

/**
 * @author Yamashita
 */
public abstract class AbstractServiceConfirmation extends AbstractService {
	/**
	 * 「商品情報を◯◯しますか？ Y/N >」という確認のメソッド
	 * 
	 * 例）「 商品情報を変更しますか？」の場合下記のように使用します
	 * ChoiceType userChoice = confirmExecution("変更");
	 * if (userChoice == ChoiceType.NO) { // Nが入力された場合
	 *   return;
	 * }
	 * 
	 * @param operationLabel 登録・変更・削除のいずれかの文字列を引数にしてください
	 * @return enum型のChoiceTypeが戻り値になります
	 * @throws UserInterruptException
	 * @author Yamashita
	 */
	protected ChoiceType confirmExecution(String operationLabel) throws UserInterruptException {

		while (true) {
			print("商品情報を" + operationLabel + "しますか？ Y/N > ");

			String userExecutionInput = getInput().trim().toUpperCase();

			ChoiceType choiceType = parseYesNoChoice(userExecutionInput);

			if (choiceType == null) {
				System.out.println("入力値が間違っています。正しい値を入力してください。");
				continue;
			}

			return choiceType;
		}
	}

	/**
	 * 入力された文字とChoiceTypeのcodeを比較
	 * @param input
	 * @return inputと一致したChoiceType
	 * @author Yamashita
	 */
	private ChoiceType parseYesNoChoice(String input) {
		for (ChoiceType ct : ChoiceType.values()) {
			if (input.equalsIgnoreCase(ct.getCode())) {
				return ct;
			}
		}
		return null;
	}
}
