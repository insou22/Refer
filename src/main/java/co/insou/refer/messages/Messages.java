package co.insou.refer.messages;

import co.insou.refer.Refer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Messages {

    private final Refer plugin;
    private static final Map<String, String> messages = new HashMap<>();

    private YamlConfiguration conf;

    public Messages(Refer plugin) {
        this.plugin = plugin;
        init();
    }

    private void init() {
        File configFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!configFile.exists()) {
            try {
                File defaultFile = new File("messages.yml");
                if (defaultFile.exists()) {
                    Files.copy(defaultFile.toPath(), configFile.toPath());
                } else {
                    System.out.println("default messages file " + defaultFile.toPath().toString() + " not found!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadDefaults();
        loadCustomMessages();

    }

    private void loadDefaults() {
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new File("messages.yml"));
        for (String key : defaultConfig.getKeys(false)) {
            messages.put(key, defaultConfig.getString(key));
        }
    }

    private void loadCustomMessages() {
        if (conf == null || conf.getKeys(false) == null) {
            plugin.log("Messages is empty");
            return;
        }
        for (String key : conf.getKeys(false)) {
            String message = conf.getString(key);
            if (message != null) {
                if (message.equalsIgnoreCase("none")) {
                    messages.put(key, "");
                    continue;
                }
                messages.put(key, message);
            }
        }
    }

    public static String getMessage(String index) {
        return reformat(messages.get(index));
    }

    public static String reformat(String message) {
        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
