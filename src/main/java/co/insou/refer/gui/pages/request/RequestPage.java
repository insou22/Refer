package co.insou.refer.gui.pages.request;

import co.insou.refer.Refer;
import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.PageInventory;
import co.insou.refer.gui.pages.ConfirmationType;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import co.insou.refer.utils.item.SkullMetaBuilder;
import mkremins.fanciful.FancyMessage;
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
public class RequestPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Request";
    private final ReferPlayer player;

    public RequestPage(ReferPlayer player) {
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
            ReferPlayer referPlayer = ((Refer) Bukkit.getPluginManager().getPlugin("Refer")).getReferPlayerManager().getReferPlayer(player);
            if (referPlayer == null) {
                throw new IllegalStateException("ReferPlayer is null");
            }
            if (!referPlayer.canRefer()) {
                continue;
            }
            ItemBuilder skull = new ItemBuilder(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal());
            skull.withMeta(
                    new SkullMetaBuilder(skull)
                            .withOwner(player.getName())
                            .withDisplayName(ChatColor.GREEN + "Click to request " + player.getName())
                            .withLore(
                                    ChatColor.AQUA + "They will be given",
                                    ChatColor.AQUA + "a notification asking",
                                    ChatColor.AQUA + "them to refer you"
                            )
            );
            items.add(skull.build());
        }
        if (items.size() == 0) {
            ItemBuilder wool = new ItemBuilder(Material.WOOL).withDurability(14);
            wool.withMeta(
                    new MetaBuilder(wool)
                            .withDisplayName(ChatColor.RED + "No requestable players online!")
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
                String name = e.getCurrentItem().getItemMeta().getDisplayName().substring((ChatColor.GREEN + "Click to request ").length());
                Player toRefer = Bukkit.getPlayer(name);
                if (toRefer == null) {
                    player.openPage(new RequestConfirmationPage(player, ConfirmationType.PLAYER_NOT_ONLINE));
                    return;
                }
                Refer refer = (Refer) Bukkit.getPluginManager().getPlugin("Refer");
                ReferPlayer toReferPlayer = refer.getReferPlayerManager().getReferPlayer(toRefer);
                if (toReferPlayer == null) {
                    player.openPage(new RequestConfirmationPage(player, ConfirmationType.INTERNAL_ERROR));
                    return;
                }
                if (!toReferPlayer.canRefer()) {
                    player.openPage(new RequestConfirmationPage(player, ConfirmationType.CANNOT_REFER_PLAYER));
                    return;
                }
                toRefer.sendMessage(ChatColor.GREEN + "Were you brought to the server by " + player.getPlayer().getName() + "?");
                new FancyMessage("If so, click here to refer them!")
                        .color(ChatColor.AQUA)
                        .style(ChatColor.ITALIC)
                        .tooltip(ChatColor.AQUA + "Click to refer!")
                        .command("/refer")
                        .send(toRefer);
                player.openPage(new RequestConfirmationPage(player, ConfirmationType.SUCCESS));
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public GUIPageType getType() {
        return GUIPageType.REQUEST_SELECT;
    }

}
