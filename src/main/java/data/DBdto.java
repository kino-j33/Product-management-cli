package data;

/**
 * @author member
 */

public class DBdto {
	private String url;
	private String user;
	private String password;
	private String driver;

	/**
	 * @author member
	 */
	public DBdto(String url, String user, String password, String driver) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.driver = driver;
	}

	/**
	 * @author member
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @author member
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @author member
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @author member
	 */
	public String getDriver() {
		return driver;
	}

}
