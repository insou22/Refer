package co.insou.refer.events;

import co.insou.refer.Refer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import co.insou.refer.utils.ReferPlayer;

public class PlayerJoin implements Listener {

	private final Refer plugin;

	public PlayerJoin(Refer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent e) {
		plugin.getReferDatabase().registerPlayer(e.getPlayer());
	}

}
