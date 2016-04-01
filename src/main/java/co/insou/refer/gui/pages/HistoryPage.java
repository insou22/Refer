package co.insou.refer.gui.pages;

import co.insou.refer.Refer;
import co.insou.refer.database.HistoryPack;
import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.PageInventory;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.SkullMetaBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by insou on 13/02/2016.
 */
public class HistoryPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > History";
    private final ReferPlayer player;

    public HistoryPage(ReferPlayer player) {
        super(player);
        this.player = player;
        setup();
    }

    private void setup() {
        PageInventory inventory = new PageInventory(title, player.getPlayer());
        List<ItemStack> history = new ArrayList<>();
        for (HistoryPack pack : ((Refer) Bukkit.getPluginManager().getPlugin("Refer")).getReferDatabase().getHistoryForPlayer(player.getPlayer())) {
            ItemBuilder skull = new ItemBuilder(Material.SKULL_ITEM).withDurability(SkullType.PLAYER.ordinal());
            skull.withMeta(
                    new SkullMetaBuilder(skull)
                            .withOwner(pack.getReferer().getName())
                            .withDisplayName(ChatColor.GREEN + (player.getPlayer().getUniqueId().equals(pack.getReferer().getUniqueId()) ? "You referred " + pack.getReferee().getName() : pack.getReferer().getName() + " referred you"))
                            .withLore(
                                    ChatColor.AQUA + "Referer: ",
                                    ChatColor.AQUA + "  UUID: " + pack.getReferer().getUniqueId().toString(),
                                    ChatColor.AQUA + "  Name: " + pack.getReferer().getName(),
                                    ChatColor.AQUA + "Referred: ",
                                    ChatColor.AQUA + "  UUID: " + pack.getReferee().getUniqueId().toString(),
                                    ChatColor.AQUA + "  Name: " + pack.getReferee().getName(),
                                    ChatColor.AQUA + "Date: " + pack.getTimestamp().toString(),
                                    ChatColor.AQUA + "ID: " + pack.getId()
                            )
            );
            history.add(skull.build());
        }
        inventory.setPages(history);
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
        return GUIPageType.HISTORY;
    }

}
