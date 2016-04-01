package co.insou.refer.gui.pages.tutorial;

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
public class RequestHelpPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Request Help";
    private final ReferPlayer player;

    public RequestHelpPage(ReferPlayer player) {
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

        ItemBuilder gold1 = new ItemBuilder(Material.GOLD_INGOT);
        gold1.withMeta(
                new MetaBuilder(gold1)
                        .withDisplayName(ChatColor.GOLD + "Requesting References!")
                        .withLore(
                                ChatColor.AQUA + "Trying to get someone",
                                ChatColor.AQUA + "who you just brought online",
                                ChatColor.AQUA + "to refer you? Clicking the",
                                ChatColor.AQUA + "gold at the main menu and",
                                ChatColor.AQUA + "selecting the player will",
                                ChatColor.AQUA + "give them a notification asking",
                                ChatColor.AQUA + "them to refer you."
                        )
        );
        inventory.setItem(11, gold1);

        ItemBuilder gold2 = new ItemBuilder(Material.GOLD_INGOT);
        gold2.withMeta(
                new MetaBuilder(gold2)
                        .withDisplayName(ChatColor.GOLD + "Why do I want references?")
                        .withLore(
                                ChatColor.AQUA + "When you refer someone",
                                ChatColor.AQUA + "you may be given a certain",
                                ChatColor.AQUA + "reward. Bringing as many",
                                ChatColor.AQUA + "people online as you can will",
                                ChatColor.AQUA + "help the server grow and give",
                                ChatColor.AQUA + "you in game rewards in exchange."
                        )
        );
        inventory.setItem(13, gold2);

        ItemBuilder gold3 = new ItemBuilder(Material.GOLD_INGOT);
        gold3.withMeta(
                new MetaBuilder(gold3)
                        .withDisplayName(ChatColor.GOLD + "Why can't I request someone?")
                        .withLore(
                                ChatColor.AQUA + "If you cannot see someone's",
                                ChatColor.AQUA + "head in the request page, and",
                                ChatColor.AQUA + "you're sure they're online,",
                                ChatColor.AQUA + "it most likely means they have",
                                ChatColor.AQUA + "already referred someone."
                        )
        );
        inventory.setItem(15, gold3);

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
        return GUIPageType.REQUEST_HELP;
    }

}
