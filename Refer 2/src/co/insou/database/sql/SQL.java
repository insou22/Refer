package co.insou.database.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {

	
	private static Connection conn;
	private static Statement statement;
	
	private static MySQL sql;
	
	public static void initSQL (String hostname, String port, String database, String username, String pswd) throws SQLException {
		sql = new MySQL(hostname, port, database, username, pswd);
		conn = sql.getConnection();
		statement = conn.createStatement();
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS `Refer` VARCHAR(30) UUID, BOOLEAN HasReferred, INT(32) References");
	}
	
	public static ResultSet executeQuery (String sql) throws SQLException {
		return statement.executeQuery(sql);
	}
	
	public static void executeUpdate (String sql) throws SQLException {
		statement.executeUpdate(sql);
	}
	
	
}
