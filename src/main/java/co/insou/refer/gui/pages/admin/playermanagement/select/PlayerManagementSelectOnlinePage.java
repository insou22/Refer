package co.insou.refer.gui.pages.admin.playermanagement.select;

import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.PageInventory;
import co.insou.refer.gui.pages.admin.playermanagement.PlayerManagementPage;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import co.insou.refer.utils.item.SkullMetaBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by insou on 14/02/2016.
 */
public class PlayerManagementSelectOnlinePage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Management Online";
    private final ReferPlayer player;

    public PlayerManagementSelectOnlinePage(ReferPlayer player) {
        super(player);
        this.player = player;
        setup();
    }

    private void setup() {
        PageInventory inventory = new PageInventory(title, player.getPlayer());
        List<ItemStack> items = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemBuilder skull = new ItemBuilder(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal());
            skull.withMeta(
                    new SkullMetaBuilder(skull)
                            .withOwner(player.getName())
                            .withDisplayName(ChatColor.GREEN + "Manage " + player.getName())
            );
            items.add(skull.build());
        }
        if (items.size() == 0) {
            ItemBuilder wool = new ItemBuilder(Material.WOOL).withDurability(14);
            wool.withMeta(
                    new MetaBuilder(wool)
                            .withDisplayName(ChatColor.RED + "No players online!")
            );
            for (int i = 0; i < 54; i++) {
                items.add(wool.build());
            }
        }
        inventory.setPages(items);

        this.display = inventory;
    }


    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (title.equals(e.getClickedInventory().getTitle())) {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.SKULL_ITEM) {
                String name = e.getCurrentItem().getItemMeta().getDisplayName().substring((ChatColor.GREEN + "Manage ").length());
                OfflinePlayer manage = Bukkit.getOfflinePlayer(name);
                player.openPage(new PlayerManagementPage(player, manage));
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public GUIPageType getType() {
        return GUIPageType.PLAYER_MANAGEMENT_SELECT_ONLINE;
    }

}
