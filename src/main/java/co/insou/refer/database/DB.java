package co.insou.refer.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import co.insou.refer.database.sql.ConnectionPool;
import org.bukkit.entity.Player;

import co.insou.refer.database.yml.Yaml;

public class DB {

	public enum databaseType {
		yaml, sql
	}

	private databaseType type;

	private Yaml yaml;
	private ConnectionPool pool;

	public DB (databaseType type, Yaml yaml, ConnectionPool pool) {
		this.type = type;
		this.yaml = yaml;
		this.pool = pool;
	}
	
	public boolean hasPlayerReferred (Player p) {
		if (type == databaseType.sql) {
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet res = null;
			try {
				conn = pool.getConnection();
				ps = conn.prepareStatement("SELECT hasReferred FROM Refer WHERE uuid=?");
				ps.setString(1, p.getUniqueId().toString());
				res = ps.executeQuery();
				return res.next() && res.getBoolean("hasReferred");
			} catch (SQLException e) {
				return true;
			} finally {
				pool.close(conn, ps, res);
			}

		}
		return yaml.database.getBoolean(p.getUniqueId().toString() + ".hasReferred");
	}
	
}
