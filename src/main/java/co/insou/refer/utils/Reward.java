package co.insou.refer.utils;

import co.insou.refer.player.ReferPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.List;
import java.util.Random;

public class Reward {

    private int referencesRequired;

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


    public Reward(int referencesRequired) {
        this.referencesRequired = referencesRequired;
    }

    public Reward withMessage(String message) {
        this.message = message;
        return this;
    }

    public Reward withMoney(Double money) {
        this.money = money;
        return this;
    }

    public Reward withSound(String sound) {
        this.sound = Sound.valueOf(sound);
        return this;
    }

    public Reward withConsoleCommands(List<String> commands) {
        this.consoleCommands = commands;
        return this;
    }

    public Reward withPlayerCommands(List<String> commands) {
        this.playerCommands = commands;
        return this;
    }

    //CHANCE

    public Reward withChancePercentage(Double percentage) {
        this.chancePercentage = percentage;
        chance = true;
        return this;
    }

    public Reward withChanceMessageWin(String message) {
        this.chanceMessageWin = message;
        chance = true;
        return this;
    }

    public Reward withChanceMessageLose(String message) {
        this.chanceMessageLose = message;
        chance = true;
        return this;
    }

    public Reward withChanceMoneyWin(Double money) {
        this.chanceMoneyWin = money;
        chance = true;
        return this;
    }

    public Reward withChanceMoneyLose(Double money) {
        this.chanceMoneyLose = money;
        chance = true;
        return this;
    }

    public Reward withChancePlayerCommandsWin(List<String> commands) {
        this.chancePlayerCommandsWin = commands;
        chance = true;
        return this;
    }

    public Reward withChancePlayerCommandsLose(List<String> commands) {
        this.chancePlayerCommandsLose = commands;
        chance = true;
        return this;
    }

    public Reward withChanceConsoleCommandsWin(List<String> commands) {
        this.chanceConsoleCommandsWin = commands;
        chance = true;
        return this;
    }

    public Reward withChanceConsoleCommandsLose(List<String> commands) {
        this.chanceConsoleCommandsLose = commands;
        chance = true;
        return this;
    }

    public Reward withChanceSoundWin(String sound) {
        this.chanceSoundWin = Sound.valueOf(sound);
        chance = true;
        return this;
    }

    public Reward withChanceSoundLose(String sound) {
        this.chanceSoundLose = Sound.valueOf(sound);
        chance = true;
        return this;
    }

    public void rewardPlayer(ReferPlayer p) {
        p.sendFormattedMessage(message);
        p.addMoney(money);
        p.playSound(sound);
        for (String cmd : consoleCommands) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), p.replaceMessage(cmd));
        }
        for (String cmd : playerCommands) {
            p.getPlayer().performCommand(p.replaceMessage(cmd));
        }
        if (chance) {
            runChance(p);
        }
    }

    private void runChance(ReferPlayer p) {
        Random random = new Random();
        int randomInt = random.nextInt(10001);
        double randomPercentage = randomInt / 100;
        if (chancePercentage >= randomPercentage) {
            p.sendFormattedMessage(chanceMessageWin);
            p.addMoney(chanceMoneyWin);
            p.playSound(chanceSoundWin);
            for (String cmd : chanceConsoleCommandsWin) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), p.replaceMessage(cmd));
            }
            for (String cmd : chancePlayerCommandsWin) {
                p.getPlayer().performCommand(p.replaceMessage(cmd));
            }
        } else {
            p.sendFormattedMessage(chanceMessageLose);
            p.addMoney(chanceMoneyLose);
            p.playSound(chanceSoundLose);
            for (String cmd : chanceConsoleCommandsLose) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), p.replaceMessage(cmd));
            }
            for (String cmd : chancePlayerCommandsLose) {
                p.getPlayer().performCommand(p.replaceMessage(cmd));
            }
        }
    }

    public int getReferencesRequired() {
        return referencesRequired;
    }
}
