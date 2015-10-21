package co.insou.refer.commands;

import co.insou.refer.Refer;
import co.insou.refer.gui.GUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.insou.refer.utils.ReferPlayer;

public class ReferCommand implements CommandExecutor {

	private Refer plugin;

	public ReferCommand (Refer plugin) {
		this.plugin = plugin;
	}


	public boolean onCommand (CommandSender s, Command c, String l, String[] args) {
		if (l.equalsIgnoreCase("refer")) {
			if (s instanceof Player) {
				ReferPlayer p = ReferPlayer.getReferPlayer((Player) s);
				switch (args.length) {
				case 0:
					plugin.gui.openGUI(p);
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				default:
					break;
				}
			}
		}
		
		return false;
	}

}
