package co.insou.refer.database.yml;

import co.insou.refer.Refer;
import java.io.File;
import java.io.IOException;

import co.insou.refer.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Yaml extends Database {

	private Refer refer;

	public Yaml (Refer refer) {
		this.refer = refer;
		init();
	}

	private YamlConfiguration database;

	private void init () {
		File f = new File(refer.getDataFolder(), "database.yml");
		if (! refer.getDataFolder().exists()) {
			refer.getDataFolder().mkdirs();
		}
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Bukkit.getPluginManager().disablePlugin(refer);
			}
			database = new YamlConfiguration();
		} else {
			database = YamlConfiguration.loadConfiguration(f);
		}
	}

	@Override
	public boolean hasPlayerReferred(Player player) {
		return database.getBoolean(player.getUniqueId().toString() + ".hasReferred");
	}

	@Override
	public void referPlayer(Player referer, Player toRefer) {
		database.set(referer.getUniqueId().toString() + ".hasReferred", true);
		database.set(toRefer.getUniqueId().toString() + ".references", getReferences(toRefer) + 1);
	}

	@Override
	public int getReferences(Player player) {
		return database.getInt(player.getUniqueId().toString() + ".references");
	}

	@Override
	public void registerPlayer(Player player) {
		if (!database.contains(player.getUniqueId().toString())) {
			database.set(player.getUniqueId().toString() + ".hasReferred", false);
			database.set(player.getUniqueId().toString() + ".references", 0);
		}
	}
}
