package util;

public final class PrintUtil {
	private PrintUtil() {
	}

	/**
	 * ------の区切り線を表示します
	 * @author Yamashita
	 */
	public static void printSeparator() {
		println("------");
	}

	/**
	 * ============の区切り線を表示します
	 * @author Yamashita
	 */
	public static void printResultSeparator() {
		println("==================================================");
	}

	/**
	 * ************の区切り線を表示します
	 * @author Yamashita
	 */
	public static void printMenuSeparator() {
		println("**************************************************");
	}

	/**
	 * 引数を改行なしで表示します
	 * @author Yamashita
	 */
	public static void print(String str) {
		System.out.print(str);
	}

	/**
	 * 引数なし、改行ありで表示します
	 * @author Yamashita
	 */
	public static void println() {
		System.out.println();
	}

	/**
	 * 引数を改行ありで表示します
	 * @author Yamashita
	 */
	public static void println(String str) {
		System.out.println(str);
	}

	/**
	 * 型引数で渡された引数を表示します
	 * @param <T>
	 * @param obj
	 * @author Yamashita
	 */
	public static <T> void printElements(T obj) {
		System.out.println(obj);
	}
}
