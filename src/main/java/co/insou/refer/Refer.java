package co.insou.refer;

import co.insou.refer.commands.ReferCommand;

import java.util.logging.Logger;

import co.insou.refer.database.DB;
import co.insou.refer.database.sql.ConnectionPool;
import co.insou.refer.database.yml.Yaml;
import co.insou.refer.events.InventoryClick;
import co.insou.refer.events.PlayerJoin;
import co.insou.refer.events.PlayerQuit;
import co.insou.refer.gui.GUI;
import co.insou.refer.gui.InventoryAPI;
import co.insou.refer.utils.ReferPlayer;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import co.insou.refer.config.Config;

public class Refer extends JavaPlugin {
	
	/*
	 * 
	 * Refer TODO
	 * 
	 * Customizable GUI
	 * Error numbers / troubleshooting
	 * // SQL
	 * All messages
	 * Extensive API
	 * // ReferPlayer
	 * Most efficiency as possible
	 * Organized / neat coding
	 * Time limits
	 * Add chance delay
	 * // Cull the static abuse
	 * Choose player from GUI instead of chat
	 *
	 */

//	public boolean debugging = false;
	public Economy econ;

	public Config config;

	public GUI gui;
	public DB db;

	
	private enum Load {
		enabled, disabled
	}

	private Listener[] events = {
		new PlayerJoin(), new PlayerQuit(), new InventoryClick(this), new InventoryAPI()
	};

	public void onDisable () {
		logLoad(Load.disabled);
	}

	public void onEnable () {
		logLoad(Load.enabled);
		initClasses();
		initConfig();
		initDatabases();
		initReferPlayers();
		initVault();
		initCommands();
		initEvents();
	}

	private void initClasses() {
		gui = new GUI(this);
	}

	public void log (String s) {
		Logger.getLogger("Refer").info("Refer: " + s);
	}

	private void logLoad (Load load) {
		log("Refer v" + getDescription().getVersion() + " has been " + load + "!");
	}
	
	private void initConfig() {
		config = new Config(this);
	}

	private boolean initDatabases () {
		if (config.sql) {
			db = new DB(DB.databaseType.sql, null, new ConnectionPool(this));
		} else {
			db = new DB(DB.databaseType.yaml, new Yaml(this), null);
		}
		return true;
	}

	private void initReferPlayers() {
		ReferPlayer.loadReferPlayers();
	}

	private void initVault() {
		if (config.vaultEnabled) {
			if (!setupEconomy()) {
				getServer().getPluginManager().disablePlugin(this);
			}
		}
	}
	
	private void initCommands () {
		getCommand("refer").setExecutor(new ReferCommand(this));
	}

	private void initEvents () {
		for (Listener event : events) {
			getServer().getPluginManager().registerEvents(event, this);
		}
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
