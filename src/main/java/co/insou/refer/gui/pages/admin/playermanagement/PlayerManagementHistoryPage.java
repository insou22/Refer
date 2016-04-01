package co.insou.refer.gui.pages.admin.playermanagement;

import co.insou.refer.Refer;
import co.insou.refer.database.HistoryPack;
import co.insou.refer.gui.page.ExternalIgnorance;
import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.PageInventory;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.SkullMetaBuilder;
import org.bukkit.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by insou on 14/02/2016.
 */
public class PlayerManagementHistoryPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Management History";
    private final ReferPlayer player;
    private final Refer refer;
    private final OfflinePlayer manage;

    public PlayerManagementHistoryPage(ReferPlayer player, OfflinePlayer manage) {
        super(player);
        this.player = player;
        this.refer = (Refer) Bukkit.getPluginManager().getPlugin("Refer");
        this.manage = manage;
        setup();
    }

    private void setup() {
        PageInventory inventory = new PageInventory(title, player.getPlayer());
        List<ItemStack> history = new ArrayList<>();
        for (HistoryPack pack : refer.getReferDatabase().getHistoryForPlayer(manage)) {
            ItemBuilder skull = new ItemBuilder(Material.SKULL_ITEM).withDurability(SkullType.PLAYER.ordinal());
            skull.withMeta(
                    new SkullMetaBuilder(skull)
                            .withOwner(pack.getReferer().getName())
                            .withDisplayName(ChatColor.GREEN + (pack.getReferer().getName() + " referred " + pack.getReferee().getName()))
                            .withLore(
                                    ChatColor.AQUA + "Referer: ",
                                    ChatColor.AQUA + "  UUID: " + pack.getReferer().getUniqueId().toString(),
                                    ChatColor.AQUA + "  Name: " + pack.getReferer().getName(),
                                    ChatColor.AQUA + "Referred: ",
                                    ChatColor.AQUA + "  UUID: " + pack.getReferee().getUniqueId().toString(),
                                    ChatColor.AQUA + "  Name: " + pack.getReferee().getName(),
                                    ChatColor.AQUA + "Date: " + pack.getTimestamp().toString(),
                                    ChatColor.AQUA + "ID: " + pack.getId(),
                                    ChatColor.AQUA + "",
                                    ChatColor.RED + "Right click to delete reference"
                            )
            );
            history.add(skull.build());
        }
        inventory.setPages(history);

        this.display = inventory;
    }


    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCursor() == null) {
            return;
        }
        ItemStack is = e.getCursor();
        if (!e.getClickedInventory().equals(e.getView().getTopInventory())) {
            return;
        }
        if (is.getType() != Material.SKULL_ITEM) {
            return;
        }
        if (e.getClick() != ClickType.RIGHT) {
            return;
        }
        int id;
        try {
            id = Integer.parseInt(is.getItemMeta().getLore().get(7).substring((ChatColor.AQUA + "ID: ").length()));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            return;
        }
        refer.getReferDatabase().deleteHistory(id);
        refresh();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public GUIPageType getType() {
        return GUIPageType.PLAYER_MANAGEMENT_HISTORY;
    }

    private void refresh() {
        player.addExternalIgnore(ExternalIgnorance.PLAYER_MANAGEMENT_HISTORY);
        player.openUndocumentedPage(new PlayerManagementHistoryPage(player, manage));
        player.removeExternalIgnore(ExternalIgnorance.PLAYER_MANAGEMENT_HISTORY);
    }

}
