package co.insou.refer.gui.pages.admin.playermanagement.select;

import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.anvil.*;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by insou on 14/02/2016.
 */
public class PlayerManagementSelectOfflinePage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Management Offline";
    private final ReferPlayer player;

    public PlayerManagementSelectOfflinePage(ReferPlayer player) {
        super(player);
        this.player = player;
        setup();
    }

    private void setup() {
        AnvilGUI gui = AnvilGUI.getAnvilGUI(player.getPlayer(), new AnvilClickEventHandler() {
            @Override
            public void onAnvilClick(final AnvilClickEvent event) {
                event.getEvent().setCancelled(true);
                if (event.getSlot() == AnvilSlot.OUTPUT) {
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                    OfflinePlayer manage = Bukkit.getOfflinePlayer(event.getName());
                    player.openUndocumentedPage(new PlayerManagementSelectionOfflineConfirmationPage(player, event.getName(), manage));
                    checkForTags();
                } else {
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                }
            }
        }, new AnvilCloseEventHandler() {
            @Override
            public void onAnvilClose(AnvilCloseEvent event) {
                if (!event.isPlanned()) {
                    checkForTags();
                }
            }
        });
        ItemBuilder tag = new ItemBuilder(Material.NAME_TAG);
        tag.withMeta(
                new MetaBuilder(tag)
                        .withDisplayName("Player name")
                        .withLore(
                                ChatColor.AQUA + "Type the name of",
                                ChatColor.AQUA + "the player that you",
                                ChatColor.AQUA + "wish to manage"
                        )
        );
        gui.setSlot(AnvilSlot.INPUT_LEFT, tag.build());
        this.display = gui;
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
        return GUIPageType.PLAYER_MANAGEMENT_SELECT_OFFLINE;
    }

    private void checkForTags() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Refer"), new Runnable() {
            @Override
            public void run() {
                for (Entity entity : player.getPlayer().getNearbyEntities(10, 10, 10)) {
                    if (entity instanceof Item) {
                        ItemStack is = ((Item) entity).getItemStack();
                        if (is.getType() == Material.NAME_TAG) {
                            if (is.hasItemMeta()) {
                                if (is.getItemMeta().hasLore()) {
                                    if (is.getItemMeta().getLore().get(0).equals(ChatColor.AQUA + "Type the name of")) {
                                        entity.remove();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 5);
    }

}
