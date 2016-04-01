package co.insou.refer.listeners.player;

import co.insou.refer.Refer;
import co.insou.refer.player.ReferPlayer;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoin implements Listener {

    private final Refer plugin;

    public PlayerJoin(Refer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        plugin.getReferDatabase().registerPlayer(player);
        plugin.getReferPlayerManager().onPlayerJoin(player);
        ReferPlayer rp = plugin.getReferPlayerManager().getReferPlayer(player);
        if (plugin.getReferConfig().isJoinMessageEnabled()) {
            if (rp.canRefer()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new FancyMessage(ChatColor.translateAlternateColorCodes('&', plugin.getReferConfig().getJoinMessage()))
                                .color(ChatColor.AQUA)
                                .style(ChatColor.ITALIC)
                                .tooltip(ChatColor.translateAlternateColorCodes('&', plugin.getReferConfig().getJoinHoverMessage()))
                                .command("/refer")
                                .send(player);
                    }
                }.runTaskLater(plugin, plugin.getReferConfig().getJoinMessageDelay());
            }
        }
    }

}
