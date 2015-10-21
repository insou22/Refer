package co.insou.refer.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import co.insou.refer.utils.ReferPlayer;

public class PlayerQuit implements Listener {
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent e) {
		ReferPlayer.removeReferPlayer(new ReferPlayer(e.getPlayer()));
	}

}
