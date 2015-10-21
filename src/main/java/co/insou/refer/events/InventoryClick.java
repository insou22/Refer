package co.insou.refer.events;

import co.insou.refer.Refer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by insou on 10/10/2015.
 */
public class InventoryClick implements Listener {

    private Refer plugin;

    public InventoryClick (Refer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent e) {
        if (e.getClickedInventory().getName().equalsIgnoreCase(plugin.config.guiTitle)) {
            e.setCancelled(true);

        }
    }

}
