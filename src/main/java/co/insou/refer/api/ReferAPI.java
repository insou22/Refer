package co.insou.refer.api;

import co.insou.refer.Refer;
import co.insou.refer.exceptions.runtime.ReferNotEnabledException;
import co.insou.refer.player.ReferPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ReferAPI {

    private final Refer plugin;

    public ReferAPI() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Refer");
        if (plugin == null) {
            throw new ReferNotEnabledException("Plugin is null - Plugin not enabled!");
        }
        if (!(plugin instanceof Refer)) {
            throw new ReferNotEnabledException("Plugin not instanceof Refer - Plugin not enabled!");
        }
        this.plugin = (Refer) plugin;
    }

    public ReferPlayer getReferPlayer(Player p) {
        return plugin.getReferPlayerManager().getReferPlayer(p);
    }

}
