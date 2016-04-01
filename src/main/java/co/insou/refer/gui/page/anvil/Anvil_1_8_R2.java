package co.insou.refer.gui.page.anvil;


import co.insou.refer.gui.page.GUIPageType;
import net.minecraft.server.v1_8_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * Created by insou on 11/01/2016.
 */
public class Anvil_1_8_R2 extends AnvilGUI {

    private class AnvilContainer extends ContainerAnvil {

        public AnvilContainer(EntityHuman entity) {
            super(entity.inventory, entity.world, new BlockPosition(0, 0, 0), entity);
        }

        @Override
        public boolean a(EntityHuman entityhuman) {
            return true;
        }

    }

    private Player player;

    private AnvilClickEventHandler clickHandler;

    private AnvilCloseEventHandler closeHandler;

    private HashMap<AnvilSlot, ItemStack> items = new HashMap<>();

    private Inventory inv;

    private Listener listener;

    private boolean closing = false;

    public Anvil_1_8_R2(Player player, final AnvilClickEventHandler clickHandler, final AnvilCloseEventHandler closeHandler) {
        this.player = player;
        this.clickHandler = clickHandler;
        this.closeHandler = closeHandler;
        this.listener = new Listener() {

            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player) {
                    Player clicker = (Player) event.getWhoClicked();

                    if (event.getInventory().equals(inv)) {
                        event.setCancelled(true);

                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";

                        if (item != null) {
                            if(item.hasItemMeta()){
                                ItemMeta meta = item.getItemMeta();

                                if (meta.hasDisplayName()) {
                                    name = meta.getDisplayName();
                                }
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name, event);

                        clickHandler.onAnvilClick(clickEvent);

                        if (clickEvent.getWillClose()) {
                            closing = true;
                            event.getWhoClicked().closeInventory();
                        }

                        if (clickEvent.getWillDestroy()) {
                            destroy();
                        }
                    }
                }
            }

            @EventHandler(priority = EventPriority.HIGHEST)
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Player player = (Player) event.getPlayer();
                    Inventory inv = event.getInventory();

                    if (inv.equals(Anvil_1_8_R2.this.inv)) {
                        AnvilCloseEvent closeEvent = new AnvilCloseEvent(event, closing);
                        closeHandler.onAnvilClose(closeEvent);
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(getPlayer())) {
                    destroy();
                }
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, Bukkit.getPluginManager().getPlugin("Editor")); //Replace with instance of main class
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
    }

    public void open() {
        EntityPlayer p = ((CraftPlayer) player).getHandle();

        AnvilContainer container = new AnvilContainer(p);

        //Set the items to the items from the inventory given
        inv = container.getBukkitView().getTopInventory();

        for (AnvilSlot slot : items.keySet()) {
            inv.setItem(slot.getSlot(), items.get(slot));
        }

        //Counter stuff that the game uses to keep track of inventories
        int c = p.nextContainerCounter();

        //Send the packet
        p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing"), 0));

        //Set their active container to the container
        p.activeContainer = container;

        //Set their active container window id to that counter stuff
        p.activeContainer.windowId = c;

        //Add the slot listener
        p.activeContainer.addSlotListener(p);
    }

    public void destroy() {
        player = null;
        clickHandler = null;
        items = null;

        HandlerList.unregisterAll(listener);

        listener = null;
    }



    @Override
    public void displayInventory() {
        open();
    }

    @Override
    public void setPageItem(int pageNumber, int slot, GUIPageType page) {

    }

    @Override
    public GUIPageType getPageItem(int pageNumber, int slot) {
        return null;
    }

    @Override
    public boolean isPageItem(int pageNumber, int slot) {
        return false;
    }

    @Override
    public GUIPageType getPageItem(int slot) {
        return null;
    }

    @Override
    public boolean isPageItem(int slot) {
        return false;
    }

}