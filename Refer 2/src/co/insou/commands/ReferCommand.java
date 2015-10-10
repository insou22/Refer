package co.insou.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.insou.gui.GUI;
import co.insou.utils.ReferPlayer;
import co.insou.utils.ReferPlayers;

public class ReferCommand implements CommandExecutor {

	public boolean onCommand (CommandSender s, Command c, String l, String[] args) {
		if (l.equalsIgnoreCase("refer")) {
			if (s instanceof Player) {
				ReferPlayer p = ReferPlayers.getReferPlayer((Player) s);
				switch (args.length) {
				case 0:
					GUI.openGUI(p);
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
