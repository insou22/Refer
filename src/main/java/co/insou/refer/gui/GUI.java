package co.insou.refer.gui;

import co.insou.refer.Refer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import co.insou.refer.config.Config;
import co.insou.refer.utils.ReferPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GUI {

	private Config config;

	public enum Function {
		REFER, REQUEST
	}

	public GUI (Refer refer) {
		this.config = refer.config;
	}

	public void openGUI (ReferPlayer p) {
		if (!p.getPlayer().hasPermission("refer.opengui")) {
			//TODO Message
			return;
		}
//		int invSize = config.guiSize;
		int invSize = 54;
		Inventory inv = Bukkit.createInventory(null, invSize, config.guiTitle);
		for (int i = 0; i < invSize; i++) {
			ItemStack is = config.guiItems.get(i);
			if (is != null) {
				ItemMeta im = is.getItemMeta();
				im.setDisplayName(p.replaceMessage(im.getDisplayName()));
				if (im.getLore() != null) {
					List<String> newLore = new ArrayList<>();
					for (String lore : im.getLore()) {
						newLore.add(p.replaceMessage(lore));
					}
					im.setLore(newLore);
				}
				inv.setItem(i, is);
			}
		}
		p.getPlayer().openInventory(inv);
	}

	public void openPlayerGUI (ReferPlayer p, Function func) {
		if (p == null || func == null) {
			return;
		}
		Collection<? extends Player> playerCollection = Bukkit.getServer().getOnlinePlayers();

		List<ItemStack> skulls = new ArrayList<>();

		for (Player player : playerCollection) {
			if (player.getUniqueId() != p.getPlayer().getUniqueId()) {
				ItemStack skull = new ItemStack(Material.SKULL, 1, (short) SkullType.PLAYER.ordinal());
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setOwner(player.getName());
				meta.setDisplayName(ChatColor.GREEN + player.getName());
				skull.setItemMeta(meta);
				skulls.add(skull);
			}
		}

		PageInventory inventory = new PageInventory(p.getPlayer());
		if (func == Function.REFER) {
			inventory.setTitle(ChatColor.GREEN + "Refer Selector!");
		} else if (func == Function.REQUEST) {
			inventory.setTitle(ChatColor.GREEN + "Request Selector!");
		} else {
			inventory.setTitle("Internal Error");
		}
		inventory.setModifiable(false);

		inventory.setPages(skulls);

		inventory.openInventory();
	}
	
}
