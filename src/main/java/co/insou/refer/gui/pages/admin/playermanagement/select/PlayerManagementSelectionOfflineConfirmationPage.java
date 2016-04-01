package co.insou.refer.gui.pages.admin.playermanagement.select;

import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.StandardInventory;
import co.insou.refer.gui.pages.admin.playermanagement.PlayerManagementPage;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by insou on 14/02/2016.
 */
public class PlayerManagementSelectionOfflineConfirmationPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Offline Confirm";

    private final ReferPlayer player;
    private OfflinePlayer manage;

    public PlayerManagementSelectionOfflineConfirmationPage(ReferPlayer player, String name, OfflinePlayer manage) {
        super(player);
        this.player = player;
        this.manage = manage;
        setup(name);
    }

    private void setup(String name) {
        // TODO - this is being skipped :(
        StandardInventory inventory = new StandardInventory(player, 54, title);

        // Background
        if (manage != null) {
            ItemBuilder background = new ItemBuilder(Material.WOOL).withDurability(5);
            background.withMeta(
                    new MetaBuilder(background)
                            .withDisplayName(ChatColor.GREEN + "Found player '" + manage.getName() + "'!")
                            .withLore(
                                    ChatColor.AQUA + "Name: " + manage.getName(),
                                    ChatColor.AQUA + "UUID: " + manage.getUniqueId(),
                                    ChatColor.AQUA + "Click to manage " + manage.getName()
                            )
            );
            for (int i = 0; i < 54; i++) {
                inventory.setItem(i, background);
            }
        } else {
            ItemBuilder background = new ItemBuilder(Material.WOOL).withDurability(14);
            background.withMeta(
                    new MetaBuilder(background)
                            .withDisplayName(ChatColor.RED + "Could not find player '" + name + "'")
            );
            for (int i = 0; i < 54; i++) {
                inventory.setItem(i, background);
            }
        }
        // End Background

        this.display = inventory;

        // Update current page, remove anvil
        player.getBreadcrumb().remove(player.getBreadcrumb().size() - 1);
        player.getBreadcrumb().add(this);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        System.out.println("PMSOCP 1");
        if (event.getCurrentItem() == null) {
            return;
        }
        System.out.println("PMSOCP 2");
        if (manage != null) {
            System.out.println("PMSOCP 3");
            player.openPage(new PlayerManagementPage(player, manage));
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public GUIPageType getType() {
        return GUIPageType.PLAYER_MANAGEMENT_SELECT_OFFLINE_CONFIRM;
    }

}
