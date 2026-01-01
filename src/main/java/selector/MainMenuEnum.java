package selector;

import service.Delete;
import service.Exit;
import service.Register;
import service.Search;
import service.ServiceProcessable;
import service.Update;

/**
 * メニューの表示やメニューから各機能のインスタンス化・メソッドの呼び出しを行うための定義を行うenum
 * @author Yamashita
 */
public enum MainMenuEnum {
	/*
	 * enum型について分かりづらいと思うので説明も載せときます！間違っているところがあったら教えて下さい
	 * enum型で宣言せずClassで同じような挙動をさせたい場合、下記のような長い記述が必要になります
	 * ※今回の場合MainMenuというクラスで作った場合を仮定しています
	 * 
	 * public static final MainMenu SEARCH   = new MainMenu("1", "検索", new Search());
	 * public static final MainMenu REGISTER = new MainMenu("2", "登録", new Register());
	 * public static final MainMenu UPDATE   = new MainMenu("3", "変更", new Update());
	 * public static final MainMenu DELETE   = new MainMenu("4", "削除", new Delete());
	 * public static final MainMenu EXIT     = new MainMenu("0", "終了", new Exit());
	 * 
	 * enumにする場合は「public static final」は暗黙的に宣言されます
	 * また右辺の「new MainMenu」などを省略して下記のような記述にできます
	 * */

	//@formatter:off
	 SEARCH(    "1"  ,"検索"   ,new Search())
	,REGISTER(  "2"  ,"登録"   ,new Register())
	,UPDATE(    "3"  ,"変更"   ,new Update())
	,DELETE(    "4"  ,"削除"   ,new Delete())
	,EXIT(      "0"  ,"終了"   ,new Exit());
	//@formatter:on
	/*
	 * 上記を他のクラスで使いたい場合は、MainMenuEnum.SEARCHとして使用できます
	 * SEARCHはインスタンス化しており、menuNumberやmenuNameなどのインスタンス変数持っています
	 * しかしprivateで宣言しているため、別途getterを設けています
	 * getterを使う場合は、MainMenuEnum　mME = MainMenuEnum.SEARCH;のように変数に入れて
	 * mME.getMenuNumber()のように呼び出すことができます。
	 * 
	 * 補足
	 * enum型のクラスを作った場合、java.lang.Enumを自動で継承するため、
	 * 他のクラスや他のenumをextendsすることはできません
	 * 
	 * enum型では自動生成されるものがいくつかあります
	 * name()   ： 定数名が自動的に入ります
	 *            そのため先程のmME変数に対してmME.name()と呼び出すとSEARCHという文字列が返ります
	 * ordinal(): 宣言された順番が入ります。今回の場合SEARCHは0、UPDATEは2のような順番となります
	 *　values() : すべての定数を配列として取得できます、MainMenuEnum.values()のように取得して
	 *            for文として利用できます。今回MainMenu.javaで利用しているので参考にしてみてください
	 * 
	 * enumクラスが最初にロードされた時点で全てのインスタンス（SEARCH, REGISTERなど）が一度に生成されます
	 * そのため各定数のコンストラクタで重い処理（例：DB接続など）を行うと、
	 * クラスロード時に遅延が発生する可能性があるそうです（知らなかった）
	 * 
	 * */

	private String menuNumber;
	private String menuName;
	private ServiceProcessable serviceProcessable;

	/**
	 * @author Yamashita
	 */
	private MainMenuEnum(String menuNumber, String menuName, ServiceProcessable serviceProcessable) {
		this.menuNumber = menuNumber;
		this.menuName = menuName;
		this.serviceProcessable = serviceProcessable;
	}

	/**
	 * @author Yamashita
	 */
	@Override
	public String toString() {
		return menuNumber + "：" + menuName;
	}

	/**
	 * @author Yamashita
	 */
	public String getMenuNumber() {
		return menuNumber;
	}

	/**
	 * @author Yamashita
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @author Yamashita
	 */
	public ServiceProcessable getServiceProcessable() {
		return serviceProcessable;
	}
}