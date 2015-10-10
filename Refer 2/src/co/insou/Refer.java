package co.insou;

import java.sql.SQLException;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import co.insou.config.Config;
import co.insou.database.DB;
import co.insou.database.sql.SQL;
import co.insou.utils.ReferPlayers;

public class Refer extends JavaPlugin {
	
	/*
	 * 
	 * Refer TODO
	 * 
	 * Customizable GUI
	 * Error numbers / troubleshooting
	 * SQL
	 * All messages
	 * Extensive API
	 * ReferPlayer
	 * Most efficiency as possible
	 * Organized / neat coding
	 * Time limits
	 * 
	 */
	
	private static Refer instance;
	
	public static boolean debugging = false;
	public static Economy econ;
	
	private enum Load {
		enabled, disabled;
	}

	@Override
	public void onDisable () {
		logLoad(Load.disabled);
	}
	
	@Override
	public void onEnable () {
		logLoad(Load.enabled);
		initInstance();
		initConfig();
		initDatabases();
		initReferPlayers();
		initVault();
		initCommands();
		initEvents();
	}

	public static Refer getInstance () {
		return instance;
	}
	
	public static void log (String s) {
		Logger.getLogger("Refer").info("Refer: " + s);
	}
	
	public static void debug (String s) {
		if (debugging) {
			Logger.getLogger("Refer").info("REFER DEBUG: " + s);
		}
	}

	private void logLoad (Load load) {
		log("Refer v" + getDescription().getVersion() + " has been " + load + "!");
	}

	private void initInstance () {
		instance = this;
	}
	
	private void initConfig() {
		Config.initConfig();
	}

	private boolean initDatabases () {
		if (Config.sql) {
			DB.setDatabaseType(DB.db.sql);
			try {
				SQL.initSQL(Config.sqlHostname, Config.sqlPort, Config.sqlDatabase, Config.sqlUsername, Config.sqlPassword);
			} catch (SQLException e) {
				log("Refer error: Cannot initSQL. Check SQL configuration!");
				getServer().getPluginManager().disablePlugin(this);
				return false;
			}
		} else {
			DB.setDatabaseType(DB.db.yaml);
		}
		return true;
	}

	private void initReferPlayers() {
		ReferPlayers.loadReferPlayers();
	}

	private void initVault() {
		if (Config.vaultEnabled) {
			if (!setupEconomy()) {
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
	}
	
	private void initCommands () {
		
	}

	private void initEvents () {
		
	}
	
	private boolean setupEconomy () {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			log("Refer error: ENABLE-VAULT is set to true, but Vault is not installed! Disabling refer!");
			return false;
		}
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider == null) {
        	log("Refer error: ENABLE-VAULT is set to true, Vault is installed, but you do not have a Vault-Supported economy plugin! Disabling refer!");
        	return false;
        }
        econ = economyProvider.getProvider();
        return econ != null;
    }
	
}
