package co.insou.utils;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class Reward {

	private String message;
	private Double money;
	private Sound sound;
	private List<String> consoleCommands;
	private List<String> playerCommands;
	
	private boolean chance = false;
	private Double chancePercentage;
	private String chanceMessageWin;
	private String chanceMessageLose;
	private Double chanceMoneyWin;
	private Double chanceMoneyLose;
	private List<String> chancePlayerCommandsWin;
	private List<String> chancePlayerCommandsLose;
	private List<String> chanceConsoleCommandsWin;
	private List<String> chanceConsoleCommandsLose;
	private Sound chanceSoundWin;
	private Sound chanceSoundLose;
	
	
	public Reward () {}
	
	public Reward withMessage (String message) {
		this.message = message;
		return this;
	}
	
	public Reward withMoney (Double money) {
		this.money = money;
		return this;
	}
	
	public Reward withSound (String sound) {
		this.sound = Sound.valueOf(sound);
		return this;
	}
	
	public Reward withConsoleCommands (List<String> commands) {
		this.consoleCommands = commands;
		return this;
	}
	
	public Reward withPlayerCommands (List<String> commands) {
		this.playerCommands = commands;
		return this;
	}
	
	//CHANCE
	
	public Reward withChancePercentage (Double percentage) {
		this.chancePercentage = percentage;
		return this;
	}
	
	public Reward withChanceMessageWin (String message) {
		this.chanceMessageWin = message;
		return this;
	}
	
	public Reward withChanceMessageLose (String message) {
		this.chanceMessageLose = message;
		return this;
	}

	public Reward withChanceMoneyWin (Double money) {
		this.chanceMoneyWin = money;
		return this;
	}
	
	public Reward withChanceMoneyLose (Double money) {
		this.chanceMoneyLose = money;
		return this;
	}

	public Reward withChancePlayerCommandsWin (List<String> commands) {
		this.chancePlayerCommandsWin = commands;
		return this;
	}

	public Reward withChancePlayerCommandsLose (List<String> commands) {
		this.chancePlayerCommandsLose = commands;
		return this;
	}

	public Reward withChanceConsoleCommandsWin (List<String> commands) {
		this.chanceConsoleCommandsWin = commands;
		return this;
	}

	public Reward withChanceConsoleCommandsLose (List<String> commands) {
		this.chanceConsoleCommandsLose = commands;
		return this;
	}
	
	public Reward withChanceSoundWin (String sound) {
		this.chanceSoundWin = Sound.valueOf(sound);
		return this;
	}

	public Reward withChanceSoundLose (String sound) {
		this.chanceSoundLose = Sound.valueOf(sound);
		return this;
	}
	
//	, String messageWin, String messageLose, Double money, List<String> playerCommandsWin, List<String> playerCommandsLose, List<String> consoleCommandsWin, List<String> consoleCommandsLose, String soundWin, String soundLose
	
	
	/*public void setChance (Double percentage, String messageWin, String messageLose, Double money, List<String> playerCommandsWin, List<String> playerCommandsLose, List<String> consoleCommandsWin, List<String> consoleCommandsLose, String soundWin, String soundLose) {
		chance = true;
		chancePercentage = percentage;
		chanceMessageWin = messageWin;
		chanceMessageLose = messageLose;
		chanceMoneyWin = money;
		chancePlayerCommandsWin = playerCommandsWin;
		chancePlayerCommandsLose = playerCommandsLose;
		chanceConsoleCommandsWin = consoleCommandsWin;
		chanceConsoleCommandsLose = consoleCommandsLose;
		chanceSoundWin = Sound.valueOf(soundWin);
		chanceSoundLose = Sound.valueOf(soundLose);
	}*/
	
	public void rewardPlayer (ReferPlayer p) {
		p.sendMessage(Replace.replace(message, p));
		p.addMoney(money);
		p.playSound(sound);
		for (String cmd : consoleCommands) {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Replace.replace(cmd, p));
		}
		for (String cmd : playerCommands) {
			p.executeCommand(Replace.replace(cmd, p));
		}
		if (chance) {
			runChance(p);
		}
	}
	
	private void runChance (ReferPlayer p) {
		Random random = new Random();
		int randomInt = random.nextInt(10001);
		double randomPercentage = randomInt / 100;
		if (chancePercentage >= randomPercentage) {
			p.sendMessage(Replace.replace(chanceMessageWin, p));
			p.addMoney(chanceMoneyWin);
			p.playSound(chanceSoundWin);
			for (String cmd : chanceConsoleCommandsWin) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Replace.replace(cmd, p));
			}
			for (String cmd : chancePlayerCommandsWin) {
				p.executeCommand(Replace.replace(cmd, p));
			}
		} else {
			p.sendMessage(Replace.replace(chanceMessageLose, p));
			p.addMoney(chanceMoneyLose);
			p.playSound(chanceSoundLose);
			for (String cmd : chanceConsoleCommandsLose) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Replace.replace(cmd, p));
			}
			for (String cmd : chancePlayerCommandsLose) {
				p.executeCommand(Replace.replace(cmd, p));
			}
		}
	}
	
}
