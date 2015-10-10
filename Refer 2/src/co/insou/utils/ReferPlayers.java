package co.insou.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReferPlayers {

	private static List<ReferPlayer> referPlayers = new ArrayList<ReferPlayer>();
	
	public static List<ReferPlayer> getReferPlayers () {
		return referPlayers;
	}
	
	public static void addReferPlayer (ReferPlayer player) {
		referPlayers.add(player);
	}
	
	public static void removeReferPlayer (ReferPlayer player) {
		referPlayers.remove(player);
	}
	
	public static void loadReferPlayers () {
		for (Player player : Bukkit.getOnlinePlayers()) {
			referPlayers.add(new ReferPlayer(player));
		}
	}
	
	public static ReferPlayer getReferPlayer (Player p) {
		for (ReferPlayer rp : getReferPlayers()) {
			if (p.getUniqueId().equals(rp.getUniqueId())) {
				return rp;
			}
		}
		ReferPlayer rp = new ReferPlayer(p);
		addReferPlayer(rp);
		return rp;
	}
	
}
