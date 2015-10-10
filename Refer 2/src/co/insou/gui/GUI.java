package co.insou.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import co.insou.config.Config;
import co.insou.utils.ReferPlayer;

public class GUI {

	public static void openGUI (ReferPlayer p) {
		if (!p.hasPermission("refer.opengui")) {
			//TODO Message
			return;
		}
		int invSize = Config.GUISize;
		Inventory inv = Bukkit.createInventory(null, invSize, Config.GUITitle);
		for (int i = 0; i < invSize; i++) {
			inv.setItem(i, Config.GUIitems.get(i));
		}
	}
	
}
