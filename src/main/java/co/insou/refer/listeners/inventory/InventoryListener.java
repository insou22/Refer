package co.insou.refer.listeners.inventory;

import co.insou.refer.Refer;
import co.insou.refer.gui.page.ClickInventory;
import co.insou.refer.gui.page.PageInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class InventoryListener implements Listener {

    private final Refer plugin;

    public InventoryListener(Refer plugin) {
        this.plugin = plugin;
    }

    private static List<ClickInventory> inventories = new ArrayList<>();

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        int found = 0;
        for (ClickInventory cInv : new ArrayList<>(inventories)) {
            if (cInv.getPlayer() == event.getWhoClicked()) {
                cInv.onInventoryClick(event);
                if (found++ == 1)
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        int found = 0;
        for (ClickInventory inv : new ArrayList<>(inventories)) {
            if (inv.getPlayer() == event.getPlayer()) {
                if (inv.isInventoryInUse()) {
                    inv.closeInventory(false);
                }
                if (found++ == 1)
                    break;
            }
        }
    }

    public static void registerInventory(ClickInventory inventory) {
        inventories.add(inventory);
    }

    public static void unregisterInventory(ClickInventory inventory) {
        inventories.remove(inventory);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        int found = 0;
        for (ClickInventory inv : new ArrayList<>(inventories)) {
            if (inv.getPlayer() == event.getWhoClicked()) {
                inv.onInventoryDrag(event);
                if (found++ == 1)
                    break;
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        int found = 0;
        for (ClickInventory inv : new ArrayList<>(inventories)) {
            if (inv.getPlayer() == event.getPlayer()) {
                if (inv.isInventoryInUse()) {
                    inv.closeInventory(false);
                }
                if (found++ == 1)
                    break;
            }
        }
    }

    public static PageInventory[] getPageInventories(Player p) {
        if (!p.hasMetadata("PageInventory")) {
            return new PageInventory[2];
        }
        return ((PageInventory[]) p.getMetadata("PageInventory").get(0).value()).clone();
    }

    /**
     * Gets the page inventory of a player
     */
    public static PageInventory getPageInventory(Player p) {
        PageInventory[] invs = getPageInventories(p);
        return invs[0];
    }

}