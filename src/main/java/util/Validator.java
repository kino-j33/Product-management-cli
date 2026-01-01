package util;

public class Validator {

	/**
	 * ⓪
	 * @return true/false
	 * @param String input ←入力する文字列　int len ←指定する文字数
	 * @author member
	 */
	public static boolean isInputOk(String input, int len) {
		if (input.length() == len) {
			return true;
		}
		return false;
	}

	//①半角英数字・_・- のみ可
	/**
	 * @author member
	 */
	public static boolean isItemId(String input) {
		if (input.matches("[0-9A-Za-z_-]+")) {
			return true;
		}
		return false;
	}

	//②10桁であること
	/**
	 * @author member
	 */
	public static boolean isItemId10(String input) {
		if (input.length() == 10) {
			return true;
		}
		return false;
	}

	//④半角数字・13桁
	/**
	 * @author member
	 */
	public static boolean isNumeric13(String input) {
		if (input.length() == 13 && input.matches("^[0-9]+$")) {
			return true;
		}
		return false;
	}

	//⑥⑧入力必須（1文字以上）
	/**
	 * @author member
	 */
	public static boolean isItemName(String input) {
		if (input.length() >= 1) {
			return true;
		}
		return false;
	}

	//⑦⑨100文字以下
	/**
	 * @author member
	 */
	public static boolean isNameOk(String input) {
		if (input.length() <= 100) {
			return true;
		}
		return false;
	}

	//⑩⑪半角数字・8桁以下
	/**
	 * @author member
	 */
	public static boolean isNumeric8(String input) {
		if (input.length() <= 8 && input.matches("^[0-9]+$")) {
			return true;
		}
		return false;
	}

	//共通　nullチェック
	/**
	 * @author member
	 */
	public static boolean isNull(String input) {
		String inputLowerCase = input.toLowerCase();
		if (inputLowerCase.contains("null")) {
			return true;
		}
		return false;
	}
}