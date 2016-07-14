package co.insou.refer.gui.pages.admin;

import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.StandardInventory;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AdminMenuPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Admin CP";
    private final ReferPlayer player;

    public AdminMenuPage(ReferPlayer player) {
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

        ItemBuilder playerManagement = new ItemBuilder(Material.SKULL_ITEM).withDurability(SkullType.PLAYER.ordinal());
        playerManagement.withMeta(
                new MetaBuilder(playerManagement)
                        .withDisplayName(ChatColor.GREEN + "Manage Players")
                        .withLore(
                                ChatColor.AQUA + "View / Edit player's information: ",
                                ChatColor.AQUA + "  - Allowed to refer",
                                ChatColor.AQUA + "  - Reference points",
                                ChatColor.AQUA + "  - Previous references / history"
                        )
        );
        inventory.setItem(11, playerManagement, GUIPageType.PLAYER_MANAGEMENT_SELECT);

        ItemBuilder history = new ItemBuilder(Material.PAPER);
        history.withMeta(
                new MetaBuilder(history)
                        .withDisplayName(ChatColor.WHITE + "Server reference history")
                        .withLore(
                                ChatColor.AQUA + "View / Edit whole server",
                                ChatColor.AQUA + "reference history."
                        )
        );
        inventory.setItem(13, history, GUIPageType.SERVER_HISTORY);

        ItemBuilder rewards = new ItemBuilder(Material.CHEST);
        rewards.withMeta(
                new MetaBuilder(rewards)
                        .withDisplayName(ChatColor.AQUA + "Rewards")
                        .withLore(
                                ChatColor.AQUA + "View / Edit / Create refer",
                                ChatColor.AQUA + "rewards in-game.",
                                ChatColor.RED + "Work-in-progress!"
                        )
        );
        inventory.setItem(15, rewards/*, GUIPageType.REWARDS_LIST*/);

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
        return GUIPageType.ADMIN_MENU;
    }

}
