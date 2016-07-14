package co.insou.refer.utils;

import co.insou.refer.Refer;
import co.insou.refer.player.ReferPlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.List;
import java.util.Random;

public class Reward {

    @Getter
    private int referencesRequired;

    @Getter
    private String message;
    @Getter
    private Double money;
    @Getter
    private Sound sound;
    @Getter
    private List<String> consoleCommands;
    @Getter
    private List<String> playerCommands;

    @Getter
    private boolean chance = false;
    @Getter
    private long chanceDelay;
    @Getter
    private Double chancePercentage;
    @Getter
    private String chanceMessageWin;
    @Getter
    private String chanceMessageLose;
    @Getter
    private Double chanceMoneyWin;
    @Getter
    private Double chanceMoneyLose;
    @Getter
    private List<String> chancePlayerCommandsWin;
    @Getter
    private List<String> chancePlayerCommandsLose;
    @Getter
    private List<String> chanceConsoleCommandsWin;
    @Getter
    private List<String> chanceConsoleCommandsLose;
    @Getter
    private Sound chanceSoundWin;
    @Getter
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
        try {
            this.sound = Sound.valueOf(sound);
        } catch (IllegalArgumentException e) {
            System.out.println("[Refer] ERROR! Tried to use a sound that didn't exist: " + sound);
        }
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
        return this;
    }

    public Reward withChanceMessageWin(String message) {
        this.chanceMessageWin = message;
        return this;
    }

    public Reward withChanceMessageLose(String message) {
        this.chanceMessageLose = message;
        return this;
    }

    public Reward withChanceMoneyWin(Double money) {
        this.chanceMoneyWin = money;
        return this;
    }

    public Reward withChanceMoneyLose(Double money) {
        this.chanceMoneyLose = money;
        return this;
    }

    public Reward withChancePlayerCommandsWin(List<String> commands) {
        this.chancePlayerCommandsWin = commands;
        return this;
    }

    public Reward withChancePlayerCommandsLose(List<String> commands) {
        this.chancePlayerCommandsLose = commands;
        return this;
    }

    public Reward withChanceConsoleCommandsWin(List<String> commands) {
        this.chanceConsoleCommandsWin = commands;
        return this;
    }

    public Reward withChanceConsoleCommandsLose(List<String> commands) {
        this.chanceConsoleCommandsLose = commands;
        return this;
    }

    public Reward withChanceSoundWin(String sound) {
        this.chanceSoundWin = Sound.valueOf(sound);
        return this;
    }

    public Reward withChanceSoundLose(String sound) {
        this.chanceSoundLose = Sound.valueOf(sound);
        return this;
    }

    public Reward withChanceDelay(long ticks) {
        this.chanceDelay = ticks;
        if (chanceDelay < 0) {
            chanceDelay = 0;
        }
        return this;
    }

    public void rewardPlayer(final ReferPlayer p) {
        p.sendFormattedMessage(message);
        if (money != null && ((Refer) Bukkit.getPluginManager().getPlugin("Refer")).isEconomyEnabled()) {
            p.addMoney(money);
        }
        if (sound != null) {
            p.playSound(sound);
        }
        if (consoleCommands != null) {
            for (String cmd : consoleCommands) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), p.replaceMessage(cmd));
            }
        }
        if (playerCommands != null) {
            for (String cmd : playerCommands) {
                p.getPlayer().performCommand(p.replaceMessage(cmd));
            }
        }
        if (chance) {
            Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("Refer"), new Runnable() {
                @Override
                public void run() {
                    runChance(p);
                }
            }, chanceDelay);
        }
    }

    private void runChance(ReferPlayer p) {
        Random random = new Random();
        int randomInt = random.nextInt(10001);
        double randomPercentage = randomInt / 100;
        if (chancePercentage >= randomPercentage) {
            p.sendFormattedMessage(chanceMessageWin);
            if (money != null && ((Refer) Bukkit.getPluginManager().getPlugin("Refer")).isEconomyEnabled()) {
                p.addMoney(chanceMoneyWin);
            }
            if (sound != null) {
                p.playSound(chanceSoundWin);
            }
            if (chanceConsoleCommandsWin != null) {
                for (String cmd : chanceConsoleCommandsWin) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), p.replaceMessage(cmd));
                }
            }
            if (chancePlayerCommandsWin != null) {
                for (String cmd : chancePlayerCommandsWin) {
                    p.getPlayer().performCommand(p.replaceMessage(cmd));
                }
            }
        } else {
            p.sendFormattedMessage(chanceMessageLose);
            if (chanceMoneyLose != null && ((Refer) Bukkit.getPluginManager().getPlugin("Refer")).isEconomyEnabled()) {
                p.addMoney(chanceMoneyLose);
            }
            if (chanceSoundLose != null) {
                p.playSound(chanceSoundLose);
            }
            if (chanceConsoleCommandsLose != null) {
                for (String cmd : chanceConsoleCommandsLose) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), p.replaceMessage(cmd));
                }
            }
            if (chancePlayerCommandsLose != null) {
                for (String cmd : chancePlayerCommandsLose) {
                    p.getPlayer().performCommand(p.replaceMessage(cmd));
                }
            }
        }
    }

}
