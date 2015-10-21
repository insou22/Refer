package co.insou.refer.gui.events;

import co.insou.refer.gui.PageInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by insou on 11/10/2015.
 */
public class PagesClickEvent extends ItemClickEvent {
    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private PageInventory inv;

    public PagesClickEvent(PageInventory inventory, int slot, InventoryClickEvent invEvent) {
        super(slot, invEvent);
        this.inv = inventory;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public PageInventory getInventory() {
        return inv;
    }

    public ItemStack getItemStack() {
        if (slot >= 0)
            return inv.getItem(slot);
        return null;
    }

    @Override
    public Player getPlayer() {
        return inv.getPlayer();
    }
}