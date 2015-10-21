package co.insou.refer.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReferPlayer {

	private static List<ReferPlayer> referPlayers = new ArrayList<>();

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
			if (p.getUniqueId() == rp.getPlayer().getUniqueId()) {
				return rp;
			}
		}
		ReferPlayer rp = new ReferPlayer(p);
		addReferPlayer(rp);
		return rp;
	}

	private Player player;
	private int references;
	
	public ReferPlayer (Player p) {
		this.player = p;
	}
	
	public int getReferences () {
		return references;
	}
	
	public void setReferences (int references) {
		this.references = references;
	}

	public Player getPlayer () {
		return player;
	}
	
	public String getName() {
		return player.getName();
	}

	//TODO Money

	public void addMoney (Double money) {
		
	}

	public Double getMoney () {
		return null;
	}


	public void playSound (String sound) {
		Sound s = Sound.valueOf(sound);
		if (s != null) player.playSound(player.getLocation(), s, 1, 1);
	}

	public String replaceMessage (String message) {
		if (message == null) {
			return null;
		}
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = message.replaceAll("%name%", player.getName())
						 .replaceAll("%references%", String.valueOf(references))
						 .replaceAll("%money%", String.valueOf(getMoney()));
		return message;
	}
}
