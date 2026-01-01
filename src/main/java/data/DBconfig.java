package data;

import java.io.InputStream;
import java.util.Properties;

public class DBconfig {

	public static DBdto loadConfig() {
		Properties properties = new Properties();

		try (InputStream input = DBconfig.class.getClassLoader().getResourceAsStream("db.properties")) {

			if (input == null) {
				System.out.println("db.properties が見つかりません。");
				return null;
			}

			properties.load(input);

			DBdto dbInfo = new DBdto(
					properties.getProperty("jdbc.url"),
					properties.getProperty("jdbc.username"),
					properties.getProperty("jdbc.password"),
					properties.getProperty("jdbc.driver"));

			return dbInfo;

		} catch (Exception e) {
			System.out.println("DB設定ファイルの読み込みに失敗しました。");
		}
		return null;
	}
}