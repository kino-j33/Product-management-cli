package util;

import static util.PrintUtil.*;

import java.util.Scanner;

import exception.UserInterruptException;

/**
 * @author member
 */
public class ScannerUtil {

	/**
	 * @author member
	 */
	private static final Scanner scanner = new Scanner(System.in);

	/**
	 * @author member
	 */
	public static String getInput() throws UserInterruptException {
		while (true) {
			String input = scanner.nextLine();

			//共通仕様 //","使用不可
			if (input.contains(",")) {
				println("カンマは使用できません。");
				continue;
			}
			//nullチェックは各項目で

			//Exitと\qが入力されている時
			String inputLowerCase = input.toLowerCase();//大文字で入れた場合も小文字に変換できる
			if (inputLowerCase.equals("exit") || inputLowerCase.equals("\\q")) {
				throw new UserInterruptException();
			}

			// 有効な入力を返す
			return input;
		}
	}

	/**
	 * @author member
	 */
	public static void close() {
		scanner.close();
	}
}
