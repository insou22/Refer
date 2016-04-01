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
public class ReferHelpPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Refer Help";
    private final ReferPlayer player;

    public ReferHelpPage(ReferPlayer player) {
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

        ItemBuilder emerald1 = new ItemBuilder(Material.EMERALD);
        emerald1.withMeta(
                new MetaBuilder(emerald1)
                        .withDisplayName(ChatColor.GREEN + "Why should I refer someone?")
                        .withLore(
                                ChatColor.AQUA + "If you were brought to",
                                ChatColor.AQUA + "the server by someone,",
                                ChatColor.AQUA + "you should consider referring",
                                ChatColor.AQUA + "them as a means of thanks."
                        )
        );
        inventory.setItem(11, emerald1);

        ItemBuilder emerald2 = new ItemBuilder(Material.EMERALD);
        emerald2.withMeta(
                new MetaBuilder(emerald2)
                        .withDisplayName(ChatColor.GREEN + "Referring someone")
                        .withLore(
                                ChatColor.AQUA + "Referring someone is as",
                                ChatColor.AQUA + "easy as clicking the emerald",
                                ChatColor.AQUA + "from the main menu, and then",
                                ChatColor.AQUA + "pressing the person's head.",
                                ChatColor.AQUA + "" + ChatColor.ITALIC + "Remember: They must be online!"
                        )
        );
        inventory.setItem(15, emerald2);

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
        return GUIPageType.REFER_HELP;
    }

}
