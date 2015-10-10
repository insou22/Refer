package co.insou.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import co.insou.Refer;
import co.insou.utils.Reward;

public class Config {
	
	public static boolean vaultEnabled = false;
	public static boolean sql = false;
	public static Sound requestSound;
	
	public static String sqlHostname;
	public static String sqlPort;
	public static String sqlDatabase;
	public static String sqlUsername;
	public static String sqlPassword;
	
	public static String GUITitle;
	public static int GUISize;
	public static Map<Integer, ItemStack> GUIitems = new HashMap<Integer, ItemStack>();
	
	private static YamlConfiguration conf;
	
	private static int getRewardNum;

	public static void initConfig () {
		File configFile = new File(Refer.getInstance().getDataFolder(), "config.yml");
		if (!Refer.getInstance().getDataFolder().exists()) {
			Refer.getInstance().getDataFolder().mkdirs();
		}
		if (!configFile.exists()) {
			Refer.getInstance().saveDefaultConfig();
		} 
		conf = YamlConfiguration.loadConfiguration(configFile);
		UpdateConfig.updateConfig(conf, conf.getDouble("config-version"));
		setConfigValues();
	}
	
	private static void setConfigValues () {
		vaultEnabled = conf.getBoolean("vault-enabled");
		sql = conf.getBoolean("sql.enabled");
		requestSound = Sound.valueOf(conf.getString("request-sound"));
		if (sql) {
			sqlHostname = conf.getString("sql.hostname");
			sqlPort = conf.getString("sql.port");
			sqlDatabase = conf.getString("sql.database");
			sqlUsername = conf.getString("sql.username");
			sqlPassword = conf.getString("sql.password");
		}
		Refer.debugging = conf.getBoolean("debug");
		GUITitle = conf.getString("gui.title").replaceAll("&", "§");
		GUISize = (conf.getInt("gui.size") / 9) * 9;
		for (int i = 0; i < GUISize; i++) {
			if (conf.contains("gui.items." + i)) {
				Material mat = Material.valueOf(conf.getString("gui.items." + i + ".material"));
				if (mat != null) {
					ItemStack is = new ItemStack(mat);
					ItemMeta im = is.getItemMeta();
					String name = conf.getString("gui.items." + i + ".name").replaceAll("&", "§");					
					List<String> lore = conf.getStringList("gui.items." + i + ".lore");
					for (int num = 0; num < lore.size(); num++) {
						lore.set(num , lore.get(num).replaceAll("&", "§"));
					}
					if (name != null) im.setDisplayName(name);
					if (lore != null) im.setLore(lore);
					is.setItemMeta(im);
					GUIitems.put(i, is);
					continue;
				}
			}
			GUIitems.put(i, new ItemStack(Material.AIR));
		}
		
	}
	
	public static List<Reward> getRewards () {
		
		List<Reward> rewards = new ArrayList<Reward>();
		
		for (String number : conf.getConfigurationSection("rewards").getKeys(false)) {
			Integer num = Integer.parseInt(number);
			if (num != null) {
				Reward reward = getReward(num);
				rewards.add(reward);
			}
		}
		return rewards;
	}
	
	@SuppressWarnings("unchecked")
	public static Reward getReward(int number) {
		getRewardNum = number;

		Reward reward = new Reward()
		.withMessage((String) get("message"))
		.withMoney((Double) get("money"))
		.withSound((String) get("sound"))
		.withConsoleCommands((List<String>) get("console-commands"))
		.withPlayerCommands((List<String>) get("console-commands"));
		
		if (conf.getBoolean(number + ".chance.enabled")) {
			
			reward = reward
					.withChancePercentage((Double) get("chance.percentage"))
					.withChanceMessageWin((String) get("chance.messages.win"))
					.withChanceMessageLose((String) get("chance.messages.lose"))
					.withChanceMoneyWin((Double) get("chance.money.win"))
					.withChanceMoneyLose((Double) get("chance.money.lose"))
					.withChancePlayerCommandsWin((List<String>) get("chance.player-commands.win"))
					.withChancePlayerCommandsLose((List<String>) get("chance.player-commands.lose"))
					.withChanceConsoleCommandsWin((List<String>) get("chance.console-commands.win"))
					.withChanceConsoleCommandsLose((List<String>) get("chance.console-commands.lose"))
					.withChanceSoundWin((String) get("chance.sounds.win"))
					.withChanceSoundLose((String) get("chance.sounds.lose"));
		}
		return reward;
	}
	
	private static Object get (String index) {
		return conf.get(getRewardNum + "." + index);
	}
	
}
