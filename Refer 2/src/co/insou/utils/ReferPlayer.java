package co.insou.utils;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ReferPlayer {

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
	
	public String getName() {
		return player.getName();
	}
	
	public void sendMessage (String message) {
		player.sendMessage(message);
	}

	public void addMoney (Double money) {
		
	}
	
	public void playSound (Sound sound) {
		player.playSound(player.getLocation(), sound, 1, 1);
	}
	
	public void playSound (String sound) {
		Sound s = Sound.valueOf(sound);
		if (s != null) player.playSound(player.getLocation(), s, 1, 1);
	}
	
	public void executeCommand (String command) {
		player.performCommand(command);
	}
	
	public UUID getUniqueId () {
		return player.getUniqueId();
	}
	
	public boolean hasPermission (String permission) {
		return player.hasPermission(permission);
	}
}
