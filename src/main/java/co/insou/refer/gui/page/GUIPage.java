package co.insou.refer.gui.page;

import co.insou.refer.player.ReferPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by insou on 10/01/2016.
 */
public abstract class GUIPage {

    protected final ReferPlayer player;

    protected ReferInventory display;

    public GUIPage(ReferPlayer player) {
        this.player = player;
    }

    public void display() {
        if (display == null) {
            throw new IllegalStateException("Display is null");
        }
        display.displayInventory();
    }

    public void onClose() {
        player.closePage();
        if (player.inGUI()) {
            player.openUndocumentedPage(player.getCurrentPage());
        }
    }

    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (display.isPageItem(event.getSlot())) {
            GUIPage page = player.getPage(display.getPageItem(event.getSlot()));
            if (page != null) {
                player.openPage(page);
            }
        } else {
            onInventoryClick(event);
        }
    }

    public abstract void onInventoryClick(InventoryClickEvent event);

    public abstract String getTitle();

    public abstract GUIPageType getType();

    protected void setDisplay(ReferInventory display) {
        this.display = display;
    }

}