package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

	public Connection Get_Connection() throws Exception {
		try {
			String connectionURL = "jdbc:mysql://127.0.0.1:3307/valhallagame";
			Connection connection = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, "adminBkr9g94", "lANVDsPQkS6h");
			return connection;
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

}
