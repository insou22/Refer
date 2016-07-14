package co.insou.refer;

import co.insou.refer.commands.ReferCommand;
import co.insou.refer.config.Config;
import co.insou.refer.database.Database;
import co.insou.refer.database.sql.SQLManager;
import co.insou.refer.database.yml.Yaml;
import co.insou.refer.gui.page.InventoryAPI;
import co.insou.refer.listeners.inventory.InventoryClick;
import co.insou.refer.listeners.inventory.InventoryClose;
import co.insou.refer.listeners.inventory.InventoryListener;
import co.insou.refer.listeners.misc.ReferListener;
import co.insou.refer.listeners.player.PlayerJoin;
import co.insou.refer.listeners.player.PlayerQuit;
import co.insou.refer.messages.Messages;
import co.insou.refer.player.ReferPlayerManager;
import co.insou.refer.utils.EconomyHandler;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Refer extends JavaPlugin {

	/*
     *
	 * Refer TODO
	 *
	 * Error numbers / troubleshooting
	 * // SQL
	 * All messages
	 * Extensive API
	 * // ReferPlayer
	 * Most efficiency as possible
	 * // Organized / neat coding
	 * Time limits
	 * // Add chance delay
	 * // Cull the static abuse
	 * // Choose player from GUIManager instead of chat
	 *
	 */




    // MASSIVE ARSE TODO:
    // Make all database calls synchronized
    // Make all database calls run asynchronously
    // Cache all data
    // Only make calls to the database to update




//	public boolean debugging = false;

    @Getter
    private boolean economyEnabled = false;
    private EconomyHandler economyHandler;

    private Config config;

    private Database database;

    private ReferPlayerManager referPlayerManager;

    private boolean enabling = true;
    private boolean disable = false;

    private Listener[] events = {
            new PlayerJoin(this), new PlayerQuit(this), new InventoryClick(this), new InventoryAPI(),
            new InventoryClick(this), new InventoryClose(this), new ReferListener(this),
            new InventoryListener(this)
    };

    @Override
    public void onDisable() {
        referPlayerManager.onDisable();
    }

    @Override
    public void onEnable() {
        initClasses();
        if (disable) return;
        initConfig();
        if (disable) return;
        initDatabases();
        if (disable) return;
        initMessages();
        if (disable) return;
        initVault();
        if (disable) return;
        initReferPlayers();
        if (disable) return;
        initCommands();
        if (disable) return;
        initEvents();
        if (disable) return;
        checkExceptions();
    }

    private void initClasses() {
        referPlayerManager = new ReferPlayerManager(this);
    }

    public void log(String s) {
        getLogger().info(s);
    }

    private void initConfig() {
        config = new Config(this);
    }

    private boolean initDatabases() {
        if (config.isSqlEnabled()) {
            database = new SQLManager(this);
        } else {
            database = new Yaml(this);
        }
        return true;
    }

    private void initMessages() {
        new Messages(this);
    }

    private void initReferPlayers() {
        referPlayerManager.loadReferPlayers();
    }

    private void initVault() {
        if (!setupEconomy()) {
//            getServer().getPluginManager().disablePlugin(this);
//            disable = true;
            getLogger().info("Could not load Vault / Economy, disabling economy features");
        }
    }

    private void initCommands() {
        getCommand("refer").setExecutor(new ReferCommand(this));
    }

    private void initEvents() {
        for (Listener event : events) {
            getServer().getPluginManager().registerEvents(event, this);
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            log("Refer error: Vault is not installed! Disabling refer!");
            return false;
        }
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider == null) {
            log("Refer error: Vault is installed, but you do not have a Vault-Supported economy plugin! Disabling refer!");
            return false;
        }
        economyHandler = new EconomyHandler(economyProvider.getProvider());
        economyEnabled = economyHandler.isSetup();
        return economyEnabled;
    }

    private void checkExceptions() {
        enabling = false;
        if (disable) {
            Bukkit.getPluginManager().disablePlugin(this);
            getLogger().info("Refer has been disabled due to a severe internal error - Check the logs!");
        }
    }

    public Database getReferDatabase() {
        return database;
    }

    public ReferPlayerManager getReferPlayerManager() {
        return referPlayerManager;
    }

    public Config getReferConfig() {
        return config;
    }

    public boolean isEnabling() {
        return enabling;
    }

    public void disableOnLoad() {
        disable = true;
    }

    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }

}
