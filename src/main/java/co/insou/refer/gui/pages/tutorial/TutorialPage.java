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
 * Created by insou on 13/02/2016.
 */
public class TutorialPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Tutorial";
    private final ReferPlayer player;

    public TutorialPage(ReferPlayer player) {
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

        ItemBuilder emerald = new ItemBuilder(Material.EMERALD);
        emerald.withMeta(
                new MetaBuilder(emerald)
                        .withDisplayName(ChatColor.GREEN + "Referring Players!")
                        .withLore(ChatColor.AQUA + "Click to find out more!")
        );
        inventory.setItem(10, emerald, GUIPageType.REFER_HELP);

        ItemBuilder paper = new ItemBuilder(Material.PAPER);
        paper.withMeta(
                new MetaBuilder(paper)
                        .withDisplayName(ChatColor.WHITE + "Reference History!")
                        .withLore(ChatColor.AQUA + "Click to find out more!")
        );
        inventory.setItem(12, paper, GUIPageType.HISTORY_HELP);

        ItemBuilder diamond = new ItemBuilder(Material.DIAMOND);
        diamond.withMeta(
                new MetaBuilder(diamond)
                        .withDisplayName(ChatColor.AQUA + "Reference Points!")
                        .withLore(ChatColor.AQUA + "Click to find out more!")
        );
        inventory.setItem(14, diamond, GUIPageType.POINTS_HELP);

        ItemBuilder gold = new ItemBuilder(Material.GOLD_INGOT);
        gold.withMeta(
                new MetaBuilder(gold)
                        .withDisplayName(ChatColor.GOLD + "Requesting References!")
                        .withLore(ChatColor.AQUA + "Click to find out more!")
        );
        inventory.setItem(16, gold, GUIPageType.REQUEST_HELP);

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
        return GUIPageType.TUTORIAL;
    }

}
