package co.insou.refer.gui.page;

import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by insou on 17/12/2015.
 */
public class StandardInventory implements ReferInventory {

    private ReferPlayer player;
    private Inventory inventory;
    private Map<Integer, GUIPageType> pageItems;

    public StandardInventory(ReferPlayer player, int size, String title) {
        this.player = player;
        pageItems = new HashMap<>();
        inventory = Bukkit.createInventory(null, size, title);
    }

    public StandardInventory setItem(int slot, ItemBuilder item) {
        return setItem(slot, item.build());
    }

    public StandardInventory setItem(int slot, ItemBuilder item, GUIPageType page) {
        return setItem(slot, item.build(), page);
    }

    public StandardInventory setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    public StandardInventory setItem(int slot, ItemStack item, GUIPageType page) {
        inventory.setItem(slot, item);
        setPageItem(1, slot, page);
        return this;
    }

    @Override
    public void displayInventory() {
        player.getPlayer().openInventory(inventory);
    }

    @Override
    public void setPageItem(int pageNumber, int slot, GUIPageType page) {
        pageItems.put(slot, page);
    }

    @Override
    public GUIPageType getPageItem(int pageNumber, int slot) {
        return getPageItem(slot);
    }

    @Override
    public boolean isPageItem(int pageNumber, int slot) {
        return isPageItem(slot);
    }

    @Override
    public GUIPageType getPageItem(int slot) {
        return pageItems.get(slot);
    }

    @Override
    public boolean isPageItem(int slot) {
        return pageItems.get(slot) != null;
    }

}
