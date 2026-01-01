package util;

/**
 * Y/Nや1/2などの二択に関するenum
 * @author Yamashita
 */
public enum ChoiceType {
	//@formatter:off
	 YES("1","Y")
	,NO("2","N");
	//@formatter:on

	private final String NUMBER;
	private final String CODE;

	/**
	 * @author Yamashita
	 */
	private ChoiceType(String number, String code) {
		this.NUMBER = number;
		this.CODE = code;
	}

	/**
	 * @author Yamashita
	 */
	public String getNumber() {
		return NUMBER;
	}

	/**
	 * @author Yamashita
	 */
	public String getCode() {
		return CODE;
	}
}
