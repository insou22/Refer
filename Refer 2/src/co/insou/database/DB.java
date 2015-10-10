package co.insou.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import co.insou.Refer;
import co.insou.database.sql.SQL;
import co.insou.database.yml.Yaml;

public class DB {

	public static enum db {
		yaml, sql
	};
	
	private static db type;
	
	public static void setDatabaseType (db type) {
		DB.type = type;
	}
	
	public static boolean hasPlayerReferred (Player p) {
		if (type == db.sql) {
			try {
				ResultSet res = SQL.executeQuery("SELECT hasReferred FROM Refer WHERE uuid='" + p.getUniqueId().toString() + "'");
				if (res.next()) {
					return res.getBoolean("hasReferred");
				} else {
					Refer.log("Refer Error: SQLRS No next");
					return true;
				}
			} catch (SQLException e) {
				Refer.log("Refer Error: SQLException");
				return true;
			}
			
		}
		if (type == db.yaml) {
			Boolean hasReferred = Yaml.database.getBoolean(p.getUniqueId().toString() + ".hasReferred");
			if (hasReferred == null) {
				Yaml.database.set(p.getUniqueId().toString() + ".hasReferred", false);
				return false;
			}
			return hasReferred;
		}
		return true;
	}
	
}
