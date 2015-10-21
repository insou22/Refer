package co.insou.refer.config;

import org.bukkit.configuration.file.YamlConfiguration;

public class UpdateConfig {
	
	public YamlConfiguration updateConfig (YamlConfiguration config, double version) {
		if (version < 2.0) {
			config = update19to20(config);
		}
		return config;
	}
	
	public YamlConfiguration update19to20 (YamlConfiguration config) {

		return config;
	}
	
}
