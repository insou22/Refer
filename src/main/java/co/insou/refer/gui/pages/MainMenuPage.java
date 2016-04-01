package co.insou.refer.gui.pages;

import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.StandardInventory;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainMenuPage extends GUIPage {

    private final String title = ChatColor.GREEN + "Refer > Main Menu";
    private final ReferPlayer player;

    public MainMenuPage(ReferPlayer player) {
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

        if (player.canRefer()) {
            ItemBuilder emerald = new ItemBuilder(Material.EMERALD);
            emerald.withMeta(
                    new MetaBuilder(emerald)
                            .withDisplayName(ChatColor.GREEN + "Refer a Player!")
                            .withLore(
                                    ChatColor.AQUA + "Click this if you were",
                                    ChatColor.AQUA + "brought to the server by",
                                    ChatColor.AQUA + "someone else to give them",
                                    ChatColor.AQUA + "a refer point / reward!"
                            )
            );
            inventory.setItem(10, emerald, GUIPageType.REFER_MENU);
        } else {
            ItemBuilder emerald = new ItemBuilder(Material.EMERALD);
            emerald.withMeta(
                    new MetaBuilder(emerald)
                            .withDisplayName(ChatColor.GREEN + "Refer a Player!")
                            .withLore(
                                    ChatColor.RED + "You are not able to refer"
                            )
            );
            inventory.setItem(10, emerald);
        }

        ItemBuilder history = new ItemBuilder(Material.PAPER);
        history.withMeta(
                new MetaBuilder(history)
                        .withDisplayName(ChatColor.WHITE + "Refer History!")
                        .withLore(
                                ChatColor.AQUA + "Click to view your",
                                ChatColor.AQUA + "refer history - who",
                                ChatColor.AQUA + "you have referred and",
                                ChatColor.AQUA + "who has referred you"
                        )
        );
        inventory.setItem(12, history, GUIPageType.HISTORY);

        ItemBuilder diamond = new ItemBuilder(Material.DIAMOND);
        diamond.withMeta(
                new MetaBuilder(diamond)
                        .withDisplayName(ChatColor.AQUA + "You have " + player.getReferences() + " refer points!")
                        .withLore(
                                ChatColor.AQUA + "You can gain reference",
                                ChatColor.AQUA + "points by bringing friends",
                                ChatColor.AQUA + "to the server and requesting",
                                ChatColor.AQUA + "them to refer you!"
                        )
        );
        inventory.setItem(14, diamond);

        ItemBuilder gold = new ItemBuilder(Material.GOLD_INGOT);
        gold.withMeta(
                new MetaBuilder(gold)
                        .withDisplayName(ChatColor.GOLD + "Request a refer!")
                        .withLore(
                                ChatColor.AQUA + "Click this if you",
                                ChatColor.AQUA + "have brought a friend",
                                ChatColor.AQUA + "online and you want",
                                ChatColor.AQUA + "them to refer you!"
                        )
        );
        inventory.setItem(16, gold, GUIPageType.REQUEST_SELECT);

        ItemBuilder info = new ItemBuilder(Material.SIGN);
        info.withMeta(
                new MetaBuilder(info)
                        .withDisplayName(ChatColor.LIGHT_PURPLE + "Tutorial")
                        .withLore(
                                ChatColor.AQUA + "Click this to learn",
                                ChatColor.AQUA + "more about how refer",
                                ChatColor.AQUA + "works if you don't",
                                ChatColor.AQUA + "completely understand",
                                ChatColor.AQUA + "the whole system!"
                        )
        );
        inventory.setItem(22, info, GUIPageType.TUTORIAL);

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
        return GUIPageType.MAIN_MENU;
    }

}
