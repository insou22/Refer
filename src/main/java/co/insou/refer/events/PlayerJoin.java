package co.insou.refer.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import co.insou.refer.utils.ReferPlayer;

public class PlayerJoin implements Listener {
	
	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent e) {
		ReferPlayer.addReferPlayer(new ReferPlayer(e.getPlayer()));
	}

}
