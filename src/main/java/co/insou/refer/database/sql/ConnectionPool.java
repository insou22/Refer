package co.insou.refer.database.sql;

import co.insou.refer.Refer;
import org.apache.commons.dbcp.BasicDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPool {

	private BasicDataSource ds;
	
	private String SQL_URL;
	private String username;
	private String password;
	
	private int initSize;
	private int maxActive;
	private int maxWait;
	private int maxIdle;

	private Refer plugin;
	
	public ConnectionPool (Refer plugin) {
		this.plugin = plugin;
		setSQLURL(
				"jdbc:mysql://" + plugin.config.sqlHostname + ":" + plugin.config.sqlPort + "/" + plugin.config.sqlDatabase,
				plugin.config.sqlUsername,
				plugin.config.sqlPassword
		);
		setConnectionPoolOptions(
				plugin.config.initSize,
				plugin.config.maxActive,
				plugin.config.maxWait,
				plugin.config.maxIdle
		);
		setupDataSource();
	}
	
	public void setupDataSource() {
		ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl(SQL_URL);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setInitialSize(initSize);
		ds.setMaxActive(maxActive);
		ds.setMaxWait(maxWait);
		ds.setMaxIdle(maxIdle);
		
		Connection conn = null;
		try {
			plugin.log("Initializing Connection Pool (THIS MAY TAKE A WHILE)");
			conn = getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (conn == null) {
			plugin.log("Could not create connection! Disabling Refer!");
			Bukkit.getPluginManager().disablePlugin(plugin);
		} else {
			try {
				PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `Kits` (UUID VARCHAR(30) NOT NULL, Kit VARCHAR(64) NOT NULL, Cooldown BIGINT NOT NULL)");
				ps.executeUpdate();
 				close(conn, ps, null);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Connection getConnection () throws SQLException {
		return ds.getConnection();
	}

	public void setSQLURL (String SQLURL, String username, String password) {
		this.SQL_URL = SQLURL;
		this.username = username;
		this.password = password;
	}
	
	public void setConnectionPoolOptions (int initSize, int maxActive, int maxWait, int maxIdle) {
 		this.initSize = initSize;
		this.maxActive = maxActive;
		this.maxWait = maxWait;
		this.maxIdle = maxIdle;
	}
	
	public void close (Connection conn, PreparedStatement ps, ResultSet res) {
		if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
		if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
		if (res != null) try { res.close(); } catch (SQLException ignored) {}
	}
	
}
