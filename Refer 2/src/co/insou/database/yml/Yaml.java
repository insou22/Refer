package co.insou.database.yml;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import co.insou.Refer;

public class Yaml {
	
	public static YamlConfiguration database;

	public static void initYaml () throws IOException {
		File f = new File(Refer.getInstance().getDataFolder(), "database.yml");
		if (!Refer.getInstance().getDataFolder().exists()) {
			Refer.getInstance().getDataFolder().mkdirs();
		}
		if (!f.exists()) {
			f.createNewFile();
			database = new YamlConfiguration();
		} else {
			database = YamlConfiguration.loadConfiguration(f);
		}
	}
	
}
