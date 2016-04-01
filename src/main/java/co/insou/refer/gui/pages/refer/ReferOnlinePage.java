package co.insou.refer.gui.pages.refer;

import co.insou.refer.Refer;
import co.insou.refer.api.ReferEvent;
import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.PageInventory;
import co.insou.refer.gui.pages.ConfirmationType;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.Reward;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import co.insou.refer.utils.item.SkullMetaBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by insou on 13/02/2016.
 */
public class ReferOnlinePage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Refer Player";
    private final ReferPlayer player;

    public ReferOnlinePage(ReferPlayer player) {
        super(player);
        this.player = player;
        setup();
    }

    private void setup() {
        PageInventory inventory = new PageInventory(title, player.getPlayer());
        List<ItemStack> items = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().equals(this.player.getPlayer().getUniqueId())) {
                continue;
            }
            ItemBuilder skull = new ItemBuilder(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal());
            skull.withMeta(
                    new SkullMetaBuilder(skull)
                            .withOwner(player.getName())
                            .withDisplayName(ChatColor.GREEN + "Click to refer " + player.getName())
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
                String name = e.getCurrentItem().getItemMeta().getDisplayName().substring((ChatColor.GREEN + "Click to refer ").length());
                Player toRefer = Bukkit.getPlayer(name);
                if (toRefer == null) {
                    player.openPage(new ReferConfirmationPage(player, ConfirmationType.PLAYER_NOT_ONLINE));
                    return;
                }
                Refer refer = (Refer) Bukkit.getPluginManager().getPlugin("Refer");
                ReferPlayer toReferPlayer = refer.getReferPlayerManager().getReferPlayer(toRefer);
                if (toReferPlayer == null) {
                    player.openPage(new ReferConfirmationPage(player, ConfirmationType.INTERNAL_ERROR));
                    return;
                }
                ReferEvent referEvent = null;
                for (Reward reward : refer.getReferConfig().getRewards()) {
                    if (reward.getReferencesRequired() == toReferPlayer.getReferences() + 1) {
                        referEvent = new ReferEvent(player, toReferPlayer, reward);
                        break;
                    }
                }
                if (referEvent == null) {
                    referEvent = new ReferEvent(player, toReferPlayer);
                }
                Bukkit.getPluginManager().callEvent(referEvent);
                if (referEvent.isCancelled()) {
                    player.openPage(new ReferConfirmationPage(player, ConfirmationType.CANNOT_REFER_PLAYER));
                    return;
                }
                player.openPage(new ReferConfirmationPage(player, ConfirmationType.SUCCESS));
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public GUIPageType getType() {
        return GUIPageType.REFER_ONLINE;
    }

}
