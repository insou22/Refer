package co.insou.refer.gui.pages.refer;

import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.StandardInventory;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ReferMenuPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Refer Menu";
    private final ReferPlayer player;

    public ReferMenuPage(ReferPlayer player) {
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

        ItemBuilder greenWool = new ItemBuilder(Material.WOOL).withDurability(5);
        greenWool.withMeta(
                new MetaBuilder(greenWool)
                        .withDisplayName(ChatColor.GREEN + "Choose Player")
                        .withLore(
                                ChatColor.AQUA + "Click here to select",
                                ChatColor.AQUA + "the player which brought",
                                ChatColor.AQUA + "you to the server!",
                                ChatColor.AQUA + "(They must currently",
                                ChatColor.AQUA + "be online on the server)"
                        )
        );
        inventory.setItem(13, greenWool, GUIPageType.REFER_ONLINE);

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
        return GUIPageType.REFER_MENU;
    }

}
