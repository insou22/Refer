package co.insou.refer.gui.pages.admin.playermanagement;

import co.insou.refer.Refer;
import co.insou.refer.gui.page.ExternalIgnorance;
import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.StandardInventory;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import co.insou.refer.utils.item.SkullMetaBuilder;
import org.bukkit.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by insou on 14/02/2016.
 */
public class PlayerManagementPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Management Select";
    private final ReferPlayer player;
    private final OfflinePlayer manage;
    private final Refer refer;

    public PlayerManagementPage(ReferPlayer player, OfflinePlayer manage) {
        super(player);
        this.player = player;
        this.manage = manage;
        this.refer = (Refer) Bukkit.getPluginManager().getPlugin("Refer");
        setup();
    }

    private void setup() {

//        System.out.println("Breadcrumb:");
//        for (GUIPage page : player.getBreadcrumb()) {
//            System.out.println("- " + page.getClass().getName());
//        }

        StandardInventory inventory = new StandardInventory(player, 27, title);

        ItemBuilder background = new ItemBuilder(Material.STAINED_GLASS_PANE);
        background.withDurability(15).withMeta(
                new MetaBuilder(background)
                        .withDisplayName(" ")
        );

        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, background);
        }

        ItemBuilder skull = new ItemBuilder(Material.SKULL_ITEM).withDurability(SkullType.PLAYER.ordinal());
        skull.withMeta(
                new SkullMetaBuilder(skull)
                        .withOwner(manage.getName())
                        .withDisplayName(ChatColor.GREEN + manage.getName() + "'s Refer Profile")
        );
        inventory.setItem(4, skull);

        ItemBuilder diamond = new ItemBuilder(Material.DIAMOND);
        diamond.withMeta(
                new MetaBuilder(diamond)
                        .withDisplayName(ChatColor.AQUA + manage.getName() + " has " + refer.getReferDatabase().getReferences(manage) + " refer points")
        );
        inventory.setItem(11, diamond);

        ItemBuilder increaseDiamond = new ItemBuilder(Material.WOOL).withDurability(5);
        increaseDiamond.withMeta(
                new MetaBuilder(increaseDiamond)
                        .withDisplayName(ChatColor.GREEN + "Add reference points")
                        .withLore(
                                ChatColor.AQUA + "Left click to increase by 1",
                                ChatColor.AQUA + "Right click to increase by 10",
                                ChatColor.AQUA + "Scroll click to increase by 100"
                        )
        );
        inventory.setItem(2, increaseDiamond);

        ItemBuilder decreaseDiamond = new ItemBuilder(Material.WOOL).withDurability(14);
        decreaseDiamond.withMeta(
                new MetaBuilder(decreaseDiamond)
                        .withDisplayName(ChatColor.RED + "Decrease reference points")
                        .withLore(
                                ChatColor.AQUA + "Left click to decrease by 1",
                                ChatColor.AQUA + "Right click to decrease by 10",
                                ChatColor.AQUA + "Scroll click to increase by 100"
                        )
        );
        inventory.setItem(20, decreaseDiamond);

        ItemBuilder canRefer;
        if (refer.getReferDatabase().hasPlayerReferred(manage)) {
            canRefer = new ItemBuilder(Material.WOOL).withDurability(14);
            canRefer.withMeta(
                    new MetaBuilder(canRefer)
                            .withDisplayName(ChatColor.RED + manage.getName() + " has already referred someone")
                            .withLore(
                                    ChatColor.AQUA + "Click to toggle"
                            )
            );
        } else {
            canRefer = new ItemBuilder(Material.WOOL).withDurability(5);
            canRefer.withMeta(
                    new MetaBuilder(canRefer)
                            .withDisplayName(ChatColor.GREEN + manage.getName() + " has not referred anyone yet")
                            .withLore(
                                    ChatColor.AQUA + "Click to toggle"
                            )
            );
        }
        inventory.setItem(13, canRefer);

        ItemBuilder history = new ItemBuilder(Material.PAPER);
        history.withMeta(
                new MetaBuilder(history)
                        .withDisplayName(ChatColor.WHITE + manage.getName() + "'s refer history")
                        .withLore(
                                ChatColor.AQUA + "Click to view " + manage.getName() + "'s refer history"
                        )
        );
        inventory.setItem(15, history);

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
        switch (e.getSlot()) {
            case 2:
                switch (e.getClick()) {
                    case LEFT:
                        refer.getReferDatabase().setReferences(manage, refer.getReferDatabase().getReferences(manage) + 1);
                        refresh();
                        break;
                    case RIGHT:
                        refer.getReferDatabase().setReferences(manage, refer.getReferDatabase().getReferences(manage) + 10);
                        refresh();
                        break;
                    case MIDDLE:
                        refer.getReferDatabase().setReferences(manage, refer.getReferDatabase().getReferences(manage) + 100);
                        refresh();
                        break;
                }
                break;
            case 20:
                switch (e.getClick()) {
                    case LEFT:
                        refer.getReferDatabase().setReferences(manage, refer.getReferDatabase().getReferences(manage) - 1);
                        refresh();
                        break;
                    case RIGHT:
                        refer.getReferDatabase().setReferences(manage, refer.getReferDatabase().getReferences(manage) - 10);
                        refresh();
                        break;
                    case MIDDLE:
                        refer.getReferDatabase().setReferences(manage, refer.getReferDatabase().getReferences(manage) - 100);
                        refresh();
                        break;
                }
                break;
            case 13:
                refer.getReferDatabase().setCanRefer(manage, refer.getReferDatabase().hasPlayerReferred(manage));
                refresh();
                break;
            case 15:
                player.openPage(new PlayerManagementHistoryPage(player, manage));
                break;
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

    private void refresh() {
//        System.out.println("About to refresh");
        player.addExternalIgnore(ExternalIgnorance.PLAYER_MANAGEMENT_MENU);
//        System.out.println("Breadcrumb: ");
//        for (GUIPage page : player.getBreadcrumb()) {
//            System.out.println("- " + page.getClass().getName());
//        }
//        System.out.println("ExternalIgnore: ");
//        for (ExternalIgnorance ignorance : player.getExternalIgnores()) {
//            System.out.println("- " + ignorance.toString());
//        }
        player.openUndocumentedPage(new PlayerManagementPage(player, manage));
//        System.out.println("Opening now...");
        Bukkit.getScheduler().scheduleSyncDelayedTask(refer, new Runnable() {
            @Override
            public void run() {
                player.removeExternalIgnore(ExternalIgnorance.PLAYER_MANAGEMENT_MENU);
//                System.out.println("All done bruv");
            }
        }, 2);
    }

}
