package co.insou.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import co.insou.utils.ReferPlayer;
import co.insou.utils.ReferPlayers;

public class PlayerQuit implements Listener {
	
	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent e) {
		ReferPlayers.removeReferPlayer(new ReferPlayer(e.getPlayer()));
	}

}
