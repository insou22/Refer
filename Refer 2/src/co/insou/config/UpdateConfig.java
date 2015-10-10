package co.insou.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class UpdateConfig {
	
	public static YamlConfiguration updateConfig (YamlConfiguration config, double version) {
		if (version < 2.0) {
			config = update19to20(config);
		}
		return config;
	}
	
	public static YamlConfiguration update19to20 (YamlConfiguration config) {
		
		
		return config;
	}
	
}
