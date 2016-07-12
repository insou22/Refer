package co.insou.refer.gui.pages.admin;

import co.insou.refer.Refer;
import co.insou.refer.database.HistoryPack;
import co.insou.refer.gui.page.ExternalIgnorance;
import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.PageInventory;
import co.insou.refer.gui.pages.admin.playermanagement.PlayerManagementHistoryPage;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.SkullMetaBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AdminHistoryPage extends GUIPage {

    private static final String TITLE = ChatColor.GREEN + "Refer > Admin CP > History";
    private final Refer plugin;

    public AdminHistoryPage(ReferPlayer player) {
        super(player);
        plugin = (Refer) Bukkit.getPluginManager().getPlugin("Refer");
        setup();
    }

    private void setup() {
        PageInventory inventory = new PageInventory(TITLE, player.getPlayer());
        List<ItemStack> history = new ArrayList<>();
        for (HistoryPack pack : plugin.getReferDatabase().getServerHistory()) {
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
    public void onInventoryClick(InventoryClickEvent event) {
//        System.out.println(1)
        if (event.getCurrentItem() == null) {
            return;
        }
//        System.out.println(2);
        ItemStack is = event.getCurrentItem();
        if (!event.getClickedInventory().equals(event.getView().getTopInventory())) {
            return;
        }
//        System.out.println(3);
        if (is.getType() != Material.SKULL_ITEM) {
            return;
        }
//        System.out.println(4);
        if (event.getClick() != ClickType.RIGHT) {
            return;
        }
//        System.out.println(5);
        int id;
        try {
            id = Integer.parseInt(is.getItemMeta().getLore().get(7).substring((ChatColor.AQUA + "ID: ").length()));
//            System.out.println(6);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
//            System.out.println(7);
            return;
        }
//        System.out.println(8);
        plugin.getReferDatabase().deleteHistory(id);
        refresh();
    }

    private void refresh() {
        player.addExternalIgnore(ExternalIgnorance.PLAYER_MANAGEMENT_HISTORY);
        player.openUndocumentedPage(new AdminHistoryPage(player));
        player.removeExternalIgnore(ExternalIgnorance.PLAYER_MANAGEMENT_HISTORY);
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public GUIPageType getType() {
        return GUIPageType.SERVER_HISTORY;
    }
}
