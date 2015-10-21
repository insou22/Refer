package co.insou.refer.database.yml;

import co.insou.refer.Refer;
import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class Yaml {

	private Refer refer;

	public Yaml (Refer refer) {
		this.refer = refer;
		init();
	}

	public YamlConfiguration database;

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
	
}
