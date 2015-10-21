package co.insou.refer.events;

import co.insou.refer.gui.events.PagesClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Created by insou on 11/10/2015.
 */
public class PagesClick implements Listener {

    @EventHandler
    public void onPagesClick (PagesClickEvent e) {
        ItemStack player = e.getItemStack();
        if (player == null) {
            e.setCancelled(true);
            return;
        }
        if (player.getItemMeta() instanceof SkullMeta) {

        }
    }
}
