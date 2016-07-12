package co.insou.refer.gui.pages.admin.reward;

import co.insou.refer.Refer;
import co.insou.refer.gui.page.GUIPage;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.gui.page.PageInventory;
import co.insou.refer.player.ReferPlayer;
import co.insou.refer.utils.Reward;
import co.insou.refer.utils.item.ItemBuilder;
import co.insou.refer.utils.item.MetaBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AdminRewardListPage extends GUIPage {

    private static final String TITLE = ChatColor.GREEN + "Refer > Admin CP > Rewards";
    private final Refer plugin;

    public AdminRewardListPage(ReferPlayer player) {
        super(player);
        plugin = (Refer) Bukkit.getPluginManager().getPlugin("Refer");
        setup();
    }

    private void setup() {
        PageInventory inventory = new PageInventory(TITLE, player.getPlayer());
        List<ItemStack> items = new ArrayList<>();
        int count = 45;
        for (Reward reward : plugin.getReferConfig().getRewards()) {
            if (count == 45) {
                count = 0;
                ItemBuilder newReward = new ItemBuilder(Material.DIAMOND);
                newReward.withMeta(
                        new MetaBuilder(newReward)
                                .withDisplayName(ChatColor.GREEN + "Click to create new reward!")
                );
                for (int i = 0; i < 9; i++) {
                    items.add(newReward.build());
                }
            }
            ItemBuilder builder = new ItemBuilder(Material.EMERALD);
            builder.withMeta(
                    new MetaBuilder(builder)
                            .withDisplayName(ChatColor.GREEN + "References required: " + reward.getReferencesRequired())
                            .withLore(ChatColor.AQUA + "Click to edit")
            );
            items.add(builder.build());
            count++;
        }
        inventory.setPages(items);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {

    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public GUIPageType getType() {
        return GUIPageType.REWARDS_LIST;
    }
}
