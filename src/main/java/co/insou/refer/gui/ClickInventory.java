package co.insou.refer.gui;

import co.insou.refer.gui.events.PageCloseEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by insou on 11/10/2015.
 */
public abstract class ClickInventory<E> {

//    protected static JavaPlugin plugin;
    protected Inventory currentInventory;
    protected boolean inventoryInUse;
    private boolean modifiable;
    private Player player;
    private boolean playerInventoryUsed;
    private ItemStack[] previousContents;
    private String inventoryName;
    private HashMap savedData = new HashMap();

    protected void saveContents() {
        this.previousContents = getPlayer().getInventory().getContents().clone();
    }

    public Object getData(Object key) {
        return savedData.get(key);
    }

    public ClickInventory setData(Object key, Object obj) {
        if (obj == null) {
            this.savedData.remove(key);
        } else {
            this.savedData.put(key, obj);
        }
        return this;
    }

    public ClickInventory(String inventoryName, Player player) {
        this.player = player;
        if (inventoryName == null) {
            inventoryName = getClass().getSimpleName();
        }
        this.inventoryName = inventoryName;
    }

    public String getName() {
        return inventoryName;
    }

    public ClickInventory setPlayerInventory() {
        if (!isInventoryInUse()) {
            this.playerInventoryUsed = true;
        }
        return this;
    }

    public void closeInventory() {
        closeInventory(true);
    }

    protected void onInventoryDrag(InventoryDragEvent event) {
        if (!isModifiable()) {
            for (int slot : event.getRawSlots()) {
                if (checkInMenu(slot)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    protected boolean checkInMenu(int rawSlot) {
        if (isPlayerInventory()) {
            if (getPlayer().getOpenInventory().getTopInventory().getHolder() != getPlayer()) {
                rawSlot -= getPlayer().getOpenInventory().getTopInventory().getSize();
            }
            return rawSlot >= 0 && rawSlot < currentInventory.getSize();
        }
        return rawSlot < currentInventory.getSize();
    }

    public boolean isPlayerInventory() {
        return playerInventoryUsed;
    }

    protected abstract void onInventoryClick(InventoryClickEvent event);

    private void closeInventory(boolean forceClose, boolean restoreInventory) {
        InventoryAPI.unregisterInventory(this);
        inventoryInUse = false;
        if (getPlayer().hasMetadata(getClass().getSimpleName())) {
            E[] invs = (E[]) getPlayer().getMetadata(getClass().getSimpleName()).get(0).value();
            if (invs[isPlayerInventory() ? 1 : 0] == this) {
                invs[isPlayerInventory() ? 1 : 0] = null;
            }
        }
        if (this instanceof PageInventory) {
            Bukkit.getPluginManager().callEvent(new PageCloseEvent((PageInventory) this));
        }
        if (forceClose && (!isPlayerInventory() || (getPlayer().getOpenInventory().getTopInventory().equals(currentInventory)))) {
            getPlayer().closeInventory();
        }
        if (isPlayerInventory() && restoreInventory) {
            getPlayer().getInventory().clear();
            getPlayer().getInventory().setContents(previousContents);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Refer"), new Runnable() {
                public void run() {
                    getPlayer().updateInventory();
                }
            });
        }
    }

    public void closeInventory(boolean forceClose) {
        closeInventory(forceClose, true);
    }

    /**
     * Gets the item in a slot. Returns null if no item or if item is null
     */
    public ItemStack getItem(int slot) {
        if (isPlayerInventory()) {
            slot += 9;
            if (slot >= 36) {
                slot -= 36;
            }
        }
        if (currentInventory != null && currentInventory.getSize() > slot) {
            return currentInventory.getItem(slot);
        }
        return null;
    }

    protected void setItems(ItemStack[] items) {
        if (isPlayerInventory()) {
            for (int i = 0; i < items.length; i++) {
                setItem(i, items[i]);
            }
        } else {
            currentInventory.setContents(items);
        }
    }

    protected void setItem(int slot, ItemStack item) {
        if (isPlayerInventory()) {
            slot += 9;
            if (slot >= 36) {
                slot -= 36;
            }
        }
        currentInventory.setItem(slot, item);
    }

    /**
     * Gets the player using this
     */
    public Player getPlayer() {
        return player;
    }

    public boolean isInventoryInUse() {
        return this.inventoryInUse;
    }

    public boolean isModifiable() {
        return modifiable;
    }

    /**
     * Internal method to open the inventory or switch them
     */
    protected void openInv() {
        /**
         * If ever getting bugs with opening a inventory and items glitch and no itemclickevent fires. Make sure you cancel the
         * click event you used to get this.. And didn't open a new inventory as the old one closed.
         */
        boolean isSwitchingInventory = isInventoryInUse();
        ItemStack heldItem = null;
        ClickInventory[] invs = new ClickInventory[2];
        for (String inv : new String[] { "PageInventory", "NamedInventory", "AnvilInventory" }) {
            if (getPlayer().hasMetadata(inv)) {
                E[] invss = (E[]) (getPlayer().hasMetadata(inv) ? getPlayer().getMetadata(inv).get(0).value() : null);
                if (invss != null) {
                    for (int i = 0; i < 2; i++) {
                        if (invss[i] != null) {
                            invs[i] = (ClickInventory) invss[i];
                        }
                    }
                }
            }
        }
        if (!isPlayerInventory()) {
            inventoryInUse = false;
            boolean previous = false;
            if (invs[1] != null) {
                previous = invs[1].inventoryInUse;
                invs[1].inventoryInUse = false;
            }
            if (isSwitchingInventory) {
                heldItem = getPlayer().getItemOnCursor();
                getPlayer().setItemOnCursor(new ItemStack(Material.AIR));
            }
            try {
                Object player = getPlayer().getClass().getDeclaredMethod("getHandle").invoke(getPlayer());
                Class c = Class.forName(player.getClass().getName().replace("Player", "Human"));
                Object defaultContainer = c.getField("defaultContainer").get(player);
                Field activeContainer = c.getField("activeContainer");
                if (activeContainer.get(player) == defaultContainer) {
                    if (!(this instanceof AnvilInventory))
                        getPlayer().openInventory(currentInventory);
                } else {
                    // Do this so that other inventories know their time is over.
                    Class.forName("org.bukkit.craftbukkit." + c.getName().split("\\.")[3] + ".event.CraftEventFactory")
                            .getMethod("handleInventoryCloseEvent", c).invoke(null, player);
                    activeContainer.set(player, defaultContainer);
                    if (!(this instanceof AnvilInventory))
                        getPlayer().openInventory(currentInventory);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (invs[1] != null) {
                invs[1].inventoryInUse = previous;
            }
        } else {
            getPlayer().updateInventory();
            if (!isSwitchingInventory && getPlayer().getOpenInventory().getTopInventory().getHolder() == getPlayer()) {
                getPlayer().openInventory(Bukkit.createInventory(null, 0, getTitle()));
            }
        }
        if (!isSwitchingInventory) {
            InventoryAPI.registerInventory(this);
            int slot = isPlayerInventory() ? 1 : 0;
            if (invs[slot] != null) {
                if (invs[slot].inventoryInUse) {
                    invs[slot].closeInventory(false, false);
                }
                if (isPlayerInventory()) {
                    this.previousContents = invs[1].previousContents;
                }
            }
            E[] inv = (E[]) (getPlayer().hasMetadata(getClass().getSimpleName()) ? getPlayer()
                    .getMetadata(getClass().getSimpleName()).get(0).value() : (E[]) Array.newInstance(getClass(), 2));
            inv[slot] = (E) this;
            getPlayer().setMetadata(getClass().getSimpleName(), new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("Refer"), inv));
        } else {
            if (heldItem != null && heldItem.getType() != Material.AIR) {
                getPlayer().setItemOnCursor(heldItem);
                getPlayer().updateInventory();
            }
        }
        inventoryInUse = true;
    }

    public abstract String getTitle();

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    public abstract void setTitle(String newTitle);

}