package co.insou.refer.commands;

import co.insou.refer.Refer;
import co.insou.refer.gui.page.GUIPageType;
import co.insou.refer.player.ReferPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReferCommand implements CommandExecutor {

    private Refer plugin;

    public ReferCommand(Refer plugin) {
        this.plugin = plugin;
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("refer")) {
            if (sender instanceof Player) {
                ReferPlayer p = plugin.getReferPlayerManager().getReferPlayer((Player) sender);
                if (p == null) {
                    sender.sendMessage(ChatColor.RED + "Internal ReferPlayer is null!");
                    return false;
                }
                if (args.length > 0 && args[0].equalsIgnoreCase("admin")) {
                    if (!p.getPlayer().hasPermission("refer.admin")) {
                        p.sendFormattedMessage("&cYou don't have permission to do this!");
                        return false;
                    }
                    p.openPage(p.getPage(GUIPageType.ADMIN_MENU));
                    return false;
                }
                if (!p.getPlayer().hasPermission("refer.use")) {
                    p.sendFormattedMessage("&cYou don't have permission to do this!");
                    return false;
                }
                p.openPage(p.getPage(GUIPageType.MAIN_MENU));
            }
        }
        return false;
    }

}
