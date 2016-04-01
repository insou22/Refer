package co.insou.refer.gui.pages.refer;

import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.StandardInventory;
import co.insou.refer.gui.pages.ConfirmationType;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by insou on 13/02/2016.
 */
public class ReferConfirmationPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Refer Confirm";
    private final ReferPlayer player;
    private final ConfirmationType type;

    public ReferConfirmationPage(ReferPlayer player, ConfirmationType type) {
        super(player);
        this.player = player;
        this.type = type;
        setup();
    }

    private void setup() {
        StandardInventory inventory = new StandardInventory(player, 54, title);
        ItemBuilder result;
        switch (type) {
            case INTERNAL_ERROR:
                result = new ItemBuilder(Material.WOOL).withDurability(14);
                result.withMeta(
                        new MetaBuilder(result)
                                .withDisplayName(ChatColor.RED + "Internal error!")
                );
                break;
            case PLAYER_NOT_ONLINE:
                result = new ItemBuilder(Material.WOOL).withDurability(14);
                result.withMeta(
                        new MetaBuilder(result)
                                .withDisplayName(ChatColor.RED + "That player is not online!")
                );
                break;
            case CANNOT_REFER_PLAYER:
                result = new ItemBuilder(Material.WOOL).withDurability(14);
                result.withMeta(
                        new MetaBuilder(result)
                                .withDisplayName(ChatColor.RED + "You can't refer that player!")
                );
                break;
            case SUCCESS:
                result = new ItemBuilder(Material.WOOL).withDurability(5);
                result.withMeta(
                        new MetaBuilder(result)
                                .withDisplayName(ChatColor.GREEN + "Success!")
                );
                break;
            default:
                result = new ItemBuilder(Material.WOOL).withDurability(14);
                result.withMeta(
                        new MetaBuilder(result)
                                .withDisplayName(ChatColor.RED + "Internal error!")
                );
                break;
        }
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, result);
        }
        this.display = inventory;
    }


    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCursor() != null) {
            onClose();
        }
    }

    @Override
    public void onClose() {
        player.getPage(GUIPageType.CLOSE_GUI);
        player.openPage(player.getPage(GUIPageType.MAIN_MENU));
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public GUIPageType getType() {
        return GUIPageType.REFER_CONFIRM;
    }

}
