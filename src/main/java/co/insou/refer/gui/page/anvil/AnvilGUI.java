package co.insou.refer.gui.page.anvil;

import co.insou.refer.gui.page.ReferInventory;
import co.insou.refer.utils.item.Reflection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class AnvilGUI implements ReferInventory {

    public static AnvilGUI getAnvilGUI(Player player, AnvilClickEventHandler clickHandler, AnvilCloseEventHandler closeHandler) {
        switch (Reflection.getVersion()) {
            case "v1_8_R1.":
                return new Anvil_1_8_R1(player, clickHandler, closeHandler);
            case "v1_8_R2.":
                return new Anvil_1_8_R2(player, clickHandler, closeHandler);
            case "v1_8_R3.":
                return new Anvil_1_8_R3(player, clickHandler, closeHandler);
            case "v1_9_R1.":
                return new Anvil_1_9_R1(player, clickHandler, closeHandler);
            default:
                return new Anvil_1_9_R1(player, clickHandler, closeHandler);
        }
    }

    public abstract void setSlot(AnvilSlot slot, ItemStack item);

}