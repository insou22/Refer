package co.insou.refer.gui.page.anvil;

import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Created by insou on 11/01/2016.
 */
public class AnvilCloseEvent {

    private InventoryCloseEvent event;
    private boolean planned;

    protected AnvilCloseEvent(InventoryCloseEvent event, boolean planned) {
        this.event = event;
        this.planned = planned;
    }

    public InventoryCloseEvent getInventoryCloseEvent() {
        return event;
    }

    public boolean isPlanned() {
        return planned;
    }

}
