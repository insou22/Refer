package co.insou.refer.listeners.inventory;

import co.insou.refer.Refer;
import co.insou.refer.player.ReferPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Created by insou on 1/12/2015.
 */
public class InventoryClose implements Listener {

    private final Refer plugin;

    public InventoryClose(Refer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (! (e.getPlayer() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getPlayer();
        ReferPlayer player = plugin.getReferPlayerManager().getReferPlayer(p);
        if (player == null) {
            plugin.log("ReferPlayer is null InventoryCloseEvent");
            return;
        }
        if (player.inGUI()) {
            player.onInventoryClose();
        }
    }

}
