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
public class HistoryHelpPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > History Help";
    private final ReferPlayer player;

    public HistoryHelpPage(ReferPlayer player) {
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

        ItemBuilder paper = new ItemBuilder(Material.PAPER);
        paper.withMeta(
                new MetaBuilder(paper)
                        .withDisplayName(ChatColor.WHITE + "Seeing history")
                        .withLore(
                                ChatColor.AQUA + "If you wish to see your",
                                ChatColor.AQUA + "previous references you",
                                ChatColor.AQUA + "have been involved in,",
                                ChatColor.AQUA + "simply press the paper",
                                ChatColor.AQUA + "from the main menu and",
                                ChatColor.AQUA + "you will see the information."
                        )
        );
        inventory.setItem(13, paper);

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
        return GUIPageType.HISTORY_HELP;
    }

}
