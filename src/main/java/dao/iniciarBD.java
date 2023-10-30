package dao;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.IOException;

public class iniciarBD {
	
	/**
	 * Conector a la base de datos
	 */
	private static Connection conn = null;

	/**
	 * Tipo de base de datos [sqlite|mariadb|...]
	 */
	public static String typeDB = null;

	/**
	 * Constructor
	 * 
	 * Establece una conexión con la base de datos
	 */
	private iniciarBD() {
		try {
			Properties prop = new Properties();
			prop.load(new FileReader("properties.database.prop"));

			typeDB = prop.getProperty("db");
			
			String dsn = prop.getProperty("dsn");
			String user = prop.getProperty("user", "");
			String pass = prop.getProperty("pass", "");

			conn = DriverManager.getConnection(dsn, user, pass);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Devuelve una conexión a la base de datos
	 * 
	 * @return Conexión a la base de datos
	 */
	public static Connection getConnection() {
		if (conn == null) {
			new iniciarBD();
		}
		return conn;
	}

	/**
	 * Cierra la conexión
	 */
	public static void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
