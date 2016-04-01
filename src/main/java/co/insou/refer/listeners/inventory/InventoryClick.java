package co.insou.refer.listeners.inventory;

import co.insou.refer.Refer;
import co.insou.refer.player.ReferPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by insou on 10/10/2015.
 */
public class InventoryClick implements Listener {

    private Refer plugin;

    public InventoryClick(Refer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getResult() == Event.Result.DENY || ! (e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        ReferPlayer player = plugin.getReferPlayerManager().getReferPlayer(p);
        if (player == null) {
            plugin.log("InventoryClick ReferPlayer is null!");
            return;
        }
        if (player.inGUI()) {
            player.onInventoryClick(e);
        }
    }

}
