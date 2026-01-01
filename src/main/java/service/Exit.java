package service;

import util.ScannerUtil;

/**
 * @author Yamashita
 */
public class Exit implements ServiceProcessable {

	/**
	 * @author Yamashita
	 */
	@Override
	public void process() {
		ScannerUtil.close();
		System.out.println("プログラムを終了します。");
	}
}
