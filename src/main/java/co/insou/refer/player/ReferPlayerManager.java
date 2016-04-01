package co.insou.refer.player;

import co.insou.refer.Refer;
import co.insou.refer.gui.page.GUIPageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReferPlayerManager {

    private Map<Player, ReferPlayer> referPlayers;

    private final Refer plugin;

    public ReferPlayerManager(Refer plugin) {
        this.plugin = plugin;
        referPlayers = new LinkedHashMap<>();
    }

    public void onPlayerJoin(Player player) {
        referPlayers.put(player, new ReferPlayer(plugin, player));
    }

    public void onPlayerQuit(Player player) {
        referPlayers.remove(player);
    }

    public void onDisable() {
        for (ReferPlayer player : getReferPlayersInGUI()) {
            player.getPage(GUIPageType.CLOSE_GUI);
        }
        referPlayers.clear();
    }

    public void loadReferPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            onPlayerJoin(player);
            plugin.getReferDatabase().registerPlayer(player);
        }
    }

    public ReferPlayer getReferPlayer(Player player) {
        return referPlayers.get(player);
    }

    public List<ReferPlayer> getReferPlayersInGUI() {
        List<ReferPlayer> players = new ArrayList<>();
        for (ReferPlayer player : referPlayers.values()) {
            if (player.inGUI()) {
                players.add(player);
            }
        }
        return players;
    }

}
