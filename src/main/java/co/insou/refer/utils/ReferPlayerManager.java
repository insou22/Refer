package co.insou.refer.utils;

import co.insou.refer.Refer;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Zac on 16/11/2015.
 */
public class ReferPlayerManager {

    private Map<Player, ReferPlayer> referPlayers;

    private final Refer plugin;

    public ReferPlayerManager(Refer plugin) {
        this.plugin = plugin;
        referPlayers = new LinkedHashMap<>();
    }

    public void onPlayerJoin(Player player) {
        referPlayers.put(player, new ReferPlayer(player));
    }

    public void onPlayerQuit(Player player) {
        referPlayers.remove(player);
    }

    public void onDisable() {
        referPlayers.clear();
    }

}
