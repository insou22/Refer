package co.insou.refer.listeners.player;

import co.insou.refer.Refer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private final Refer plugin;

    public PlayerQuit(Refer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        plugin.getReferPlayerManager().onPlayerQuit(e.getPlayer());
    }

}
