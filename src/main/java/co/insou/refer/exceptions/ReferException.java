package co.insou.refer.exceptions;

import co.insou.refer.Refer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ReferException {

    public ReferException(int errorCode, String errorMessage, boolean severe) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Refer");
        if (plugin instanceof Refer) {
            Refer refer = (Refer) plugin;
            refer.log("Refer Error " + errorCode + ": " + errorMessage);
            if (severe) {
                refer.log("Error Severe! Disabling Refer!");
                if (refer.isEnabling()) refer.disableOnLoad();
                else {
                    Bukkit.getPluginManager().disablePlugin(refer);
                    refer.disableOnLoad();
                }
            }
        } else if (plugin != null) {
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

}
