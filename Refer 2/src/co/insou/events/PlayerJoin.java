package co.insou.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import co.insou.utils.ReferPlayer;
import co.insou.utils.ReferPlayers;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent e) {
		ReferPlayers.addReferPlayer(new ReferPlayer(e.getPlayer()));
	}

}
