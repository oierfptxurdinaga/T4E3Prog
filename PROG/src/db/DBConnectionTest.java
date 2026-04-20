package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {
	private static final String URL = "jdbc:mysql://localhost:3306/Federazioatest";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	public static Connection obtenerConexion() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
