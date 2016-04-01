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
public class PointsHelpPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Points Help";
    private final ReferPlayer player;

    public PointsHelpPage(ReferPlayer player) {
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

        ItemBuilder diamond = new ItemBuilder(Material.DIAMOND);
        diamond.withMeta(
                new MetaBuilder(diamond)
                        .withDisplayName(ChatColor.AQUA + "Refernce Points")
                        .withLore(
                                ChatColor.AQUA + "If you wish to know",
                                ChatColor.AQUA + "how many refers you've",
                                ChatColor.AQUA + "recieved, hover over the",
                                ChatColor.AQUA + "diamond at the main menu."
                        )
        );
        inventory.setItem(13, diamond);

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
        return GUIPageType.POINTS_HELP;
    }

}
