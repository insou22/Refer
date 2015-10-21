package co.insou.refer.config;

import co.insou.refer.Refer;
import co.insou.refer.utils.Reward;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Config {

	private Refer refer;
	private UpdateConfig updateConfig;

	public Config (Refer refer) {
		this.refer = refer;
		this.updateConfig = new UpdateConfig();
		init();
	}
	
	public boolean vaultEnabled = false;
	public boolean sql = false;
	public Sound requestSound;
	
	public String sqlHostname;
	public String sqlPort;
	public String sqlDatabase;
	public String sqlUsername;
	public String sqlPassword;

	public int initSize;
	public int maxActive;
	public int maxWait;
	public int maxIdle;
	
	public String guiTitle;
	public int guiSize;
	public Map<Integer, ItemStack> guiItems = new HashMap<>();

	public String playerGUITitleRefer;
	public String playerGUITitleRequest;

	private YamlConfiguration conf;
	
	private int getRewardNum;

	private void init () {
		File configFile = new File(refer.getDataFolder(), "config.yml");
		if (!refer.getDataFolder().exists()) {
			if (! refer.getDataFolder().mkdirs()) {
				refer.log("Couldn't generate Data Folder");
				Bukkit.getPluginManager().disablePlugin(refer);
			}
		}
		if (!configFile.exists()) {
			refer.saveDefaultConfig();
		} 
		conf = YamlConfiguration.loadConfiguration(configFile);
		updateConfig.updateConfig(conf, conf.getDouble("config-version"));
		setConfigValues();
	}
	
	private void setConfigValues () {
		vaultEnabled = conf.getBoolean("vault-enabled");
		sql = conf.getBoolean("sql.enabled");
		requestSound = Sound.valueOf(conf.getString("request-sound"));
		if (sql) {
			sqlHostname = conf.getString("sql.hostname");
			sqlPort = conf.getString("sql.port");
			sqlDatabase = conf.getString("sql.database");
			sqlUsername = conf.getString("sql.username");
			sqlPassword = conf.getString("sql.password");
			initSize = conf.getInt("sql.connection-pool.initial-size");
			maxActive = conf.getInt("sql.connection-pool.max-active");
			maxWait = conf.getInt("sql.connection-pool.max-wait");
			maxIdle = conf.getInt("sql.connection-pool.max-idle");
		}
		guiTitle = ChatColor.translateAlternateColorCodes('&', conf.getString("gui.title.selector"));
		playerGUITitleRefer = ChatColor.translateAlternateColorCodes('&', conf.getString("gui.title.player.refer"));
		playerGUITitleRequest = ChatColor.translateAlternateColorCodes('&', conf.getString("gui.title.player.request"));
		//TODO NPEs
		playerGUITitleRefer = "Refer Head Selector";
		playerGUITitleRequest = "Request Head Selector";
		guiSize = (conf.getInt("gui.size") / 9) * 9;
		for (int i = 0; i < guiSize; i++) {
			if (conf.contains("gui.items." + i)) {
				Material mat = Material.valueOf(conf.getString("gui.items." + i + ".material"));
				if (mat != null) {
					ItemStack is = new ItemStack(mat);
					ItemMeta im = is.getItemMeta();
					String name = ChatColor.translateAlternateColorCodes('&', conf.getString("gui.items." + i + ".name"));
					List<String> lore = conf.getStringList("gui.items." + i + ".lore");
					for (int num = 0; num < lore.size(); num++) {
						lore.set(num , ChatColor.translateAlternateColorCodes('&', lore.get(num)));
					}
					if (name.length() != 0) im.setDisplayName(name);
					if (lore.size() != 0) im.setLore(lore);
					is.setItemMeta(im);
					guiItems.put(i, is);
					continue;
				}
			}
			guiItems.put(i, new ItemStack(Material.AIR));
		}
		
	}
	
	public List<Reward> getRewards () {
		
		List<Reward> rewards = new ArrayList<>();
		
		for (String number : conf.getConfigurationSection("rewards").getKeys(false)) {
			Integer num = Integer.valueOf(number);
			if (num != null) {
				Reward reward = getReward(num);
				rewards.add(reward);
			}
		}
		return rewards;
	}

	public Reward getReward (int number) {
		getRewardNum = number;

		Reward reward = new Reward()
		.withMessage((String) get("message"))
		.withMoney((Double) get("money"))
		.withSound((String) get("sound"))
		.withConsoleCommands(getList("console-commands"))
		.withPlayerCommands(getList("player-commands"));

		if (conf.getBoolean(number + ".chance.enabled")) {
			reward = reward
					.withChancePercentage((Double) get("chance.percentage"))
					.withChanceMessageWin((String) get("chance.messages.win"))
					.withChanceMessageLose((String) get("chance.messages.lose"))
					.withChanceMoneyWin((Double) get("chance.money.win"))
					.withChanceMoneyLose((Double) get("chance.money.lose"))
					.withChancePlayerCommandsWin(getList("chance.player-commands.win"))
					.withChancePlayerCommandsLose(getList("chance.player-commands.lose"))
					.withChanceConsoleCommandsWin(getList("chance.console-commands.win"))
					.withChanceConsoleCommandsLose(getList("chance.console-commands.lose"))
					.withChanceSoundWin((String) get("chance.sounds.win"))
					.withChanceSoundLose((String) get("chance.sounds.lose"));
		}
		return reward;
	}
	
	private Object get (String index) {
		return conf.get(getRewardNum + "." + index);
	}

	private List<String> getList (String index) {
		return conf.getStringList(getRewardNum + "." + index);
	}
	
}
