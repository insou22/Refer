package co.insou.refer.gui.pages.admin.playermanagement.select;

import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.StandardInventory;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by insou on 14/02/2016.
 */
public class PlayerManagementSelectionPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Management Select";
    private final ReferPlayer player;

    public PlayerManagementSelectionPage(ReferPlayer player) {
        super(player);
        this.player = player;
        setup();
    }

    private void setup() {
        StandardInventory inventory = new StandardInventory(player, 27, title);

        ItemBuilder background = new ItemBuilder(Material.STAINED_GLASS_PANE);
        background.withDurability(15).withMeta(
                new MetaBuilder(background)
                        .withDisplayName(" ")
        );

        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, background);
        }

        ItemBuilder online = new ItemBuilder(Material.WOOL).withDurability(5);
        online.withMeta(
                new MetaBuilder(online)
                        .withDisplayName(ChatColor.GREEN + "Online")
                        .withLore(
                                ChatColor.AQUA + "Select this if the player",
                                ChatColor.AQUA + "you wish to manage is online."
                        )
        );
        inventory.setItem(11, online, GUIPageType.PLAYER_MANAGEMENT_SELECT_ONLINE);

        ItemBuilder offline = new ItemBuilder(Material.WOOL).withDurability(14);
        offline.withMeta(
                new MetaBuilder(offline)
                        .withDisplayName(ChatColor.RED + "Offline")
                        .withLore(
                                ChatColor.AQUA + "Select this if the player",
                                ChatColor.AQUA + "you wish to manage is offline."
                        )
        );
        inventory.setItem(15, offline, GUIPageType.PLAYER_MANAGEMENT_SELECT_OFFLINE);

        this.display = inventory;
    }


    @Override
    public void onInventoryClick(InventoryClickEvent e) {

    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public GUIPageType getType() {
        return GUIPageType.PLAYER_MANAGEMENT_SELECT;
    }

}
