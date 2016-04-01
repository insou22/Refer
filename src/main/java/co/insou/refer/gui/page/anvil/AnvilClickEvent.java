package co.insou.refer.gui.page.anvil;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by insou on 11/01/2016.
 */
public class AnvilClickEvent {

    private AnvilSlot slot;

    private String name;

    private InventoryClickEvent event;

    private boolean close = true;
    private boolean destroy = true;

    protected AnvilClickEvent(AnvilSlot slot, String name, InventoryClickEvent event) {
        this.slot = slot;
        this.name = name;
        this.event = event;
    }

    public AnvilSlot getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    public boolean getWillClose() {
        return close;
    }

    public void setWillClose(boolean close) {
        this.close = close;
    }

    public boolean getWillDestroy() {
        return destroy;
    }

    public void setWillDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

}