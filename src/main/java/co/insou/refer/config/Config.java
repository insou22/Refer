package co.insou.refer.config;

import co.insou.refer.Refer;
import co.insou.refer.utils.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Config {

    private Refer refer;
    private UpdateConfig updateConfig;

    public Config(Refer refer) {
        this.refer = refer;
        this.updateConfig = new UpdateConfig();
        init();
    }

    private boolean vaultEnabled = false;
    private boolean sql = false;
    private Sound requestSound;

    private boolean joinMessageEnabled = true;
    private long joinMessageDelay = 60;
    private String joinMessage = "&bWere you brought to the server by someone? Click here to refer them!";
    private String joinHoverMessage = "&bClick here to refer them!";

    private String sqlHostname;
    private String sqlPort;
    private String sqlDatabase;
    private String sqlUsername;
    private String sqlPassword;

    private int initSize;
    private int maxActive;
    private int maxWait;
    private int maxIdle;

    private YamlConfiguration conf;

    private void init() {
        File configFile = new File(refer.getDataFolder(), "config.yml");
        if (!refer.getDataFolder().exists()) {
            if (!refer.getDataFolder().mkdirs()) {
                refer.log("Couldn't generate Data Folder");
                Bukkit.getPluginManager().disablePlugin(refer);
                refer.disableOnLoad();
            }
        }
        if (!configFile.exists()) {
            refer.saveDefaultConfig();
        }
        conf = YamlConfiguration.loadConfiguration(configFile);
        updateConfig.updateConfig(conf, conf.getDouble("config-version"));
        setConfigValues();
    }

    private void setConfigValues() {
        vaultEnabled = conf.getBoolean("vault-enabled");
        sql = conf.getBoolean("sql.enabled");
        try {
            requestSound = Sound.valueOf(conf.getString("request-sound"));
        } catch (IllegalArgumentException e) {
            refer.getLogger().severe("Could not load Sound " + conf.getString("request-sound"));
            refer.getLogger().severe("Make sure it is valid and is typed correctly!");
        }
        joinMessageEnabled = conf.getBoolean("join-message.enabled");
        if (joinMessageEnabled) {
            joinMessage = conf.getString("join-message.message");
            joinHoverMessage = conf.getString("join-message.hover");
            joinMessageDelay = conf.getLong("join-message.delay");
        }
        if (sql) {
            sqlHostname = conf.getString("sql.hostname");
            sqlPort = conf.getString("sql.port");
            sqlDatabase = conf.getString("sql.database");
            sqlUsername = conf.getString("sql.username");
            sqlPassword = conf.getString("sql.password");
            initSize = conf.getInt("sql.connection-pool.initial-size");
            maxActive = conf.getInt("sql.connection-pool.max-active");
            maxWait = conf.getInt("sql.connection-pool.max-wait");
            maxIdle = conf.getInt("sql.connection-pool.max-idle");
        }
        //TODO NPEs
    }

    public List<Reward> getRewards() {

        List<Reward> rewards = new ArrayList<>();

        for (String number : conf.getConfigurationSection("rewards").getKeys(false)) {
            Integer num = Integer.valueOf(number);
            if (num != null) {
                Reward reward = getReward(num);
                rewards.add(reward);
            }
        }
        return rewards;
    }

    public Reward getReward(int number) {
        Reward reward = new Reward(number);
        reward.withMessage(getString(number, "message"));
        reward.withMoney(getDouble(number, "money"));
        reward.withSound(getString(number, "sound"));
        reward.withConsoleCommands(getStringList(number, "console-commands"));
        reward.withPlayerCommands(getStringList(number, "player-commands"));

        if (getBoolean(number, "chance.enabled")) {
            reward = reward
                    .withChancePercentage(getDouble(number, "chance.percentage"))
                    .withChanceMessageWin(getString(number, "chance.messages.win"))
                    .withChanceMessageLose(getString(number, "chance.messages.lose"))
                    .withChanceMoneyWin(getDouble(number, "chance.money.win"))
                    .withChanceMoneyLose(getDouble(number, "chance.money.lose"))
                    .withChancePlayerCommandsWin(getStringList(number, "chance.player-commands.win"))
                    .withChancePlayerCommandsLose(getStringList(number, "chance.player-commands.lose"))
                    .withChanceConsoleCommandsWin(getStringList(number, "chance.console-commands.win"))
                    .withChanceConsoleCommandsLose(getStringList(number, "chance.console-commands.lose"))
                    .withChanceSoundWin(getString(number, "chance.sounds.win"))
                    .withChanceSoundLose(getString(number, "chance.sounds.lose"));
        }
        return reward;
    }

    private double getDouble(int rewardNum, String index) {
        return conf.getDouble("rewards." + rewardNum + "." + index);
    }

    private boolean getBoolean(int rewardNum, String index) {
        return conf.getBoolean("rewards." + rewardNum + "." + index);
    }

    private String getString(int rewardNum, String index) {
        return conf.getString("rewards." + rewardNum + "." + index);
    }

    private List<String> getStringList(int rewardNum, String index) {
        return conf.getStringList("rewards." + rewardNum + "." + index);
    }

    public boolean isVaultEnabled() {
        return vaultEnabled;
    }

    public boolean isSqlEnabled() {
        return sql;
    }

    public Sound getRequestSound() {
        return requestSound;
    }

    public boolean isJoinMessageEnabled() {
        return joinMessageEnabled;
    }

    public long getJoinMessageDelay() {
        return joinMessageDelay;
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public String getJoinHoverMessage() {
        return joinHoverMessage;
    }

    public String getSqlHostname() {
        return sqlHostname;
    }

    public String getSqlPort() {
        return sqlPort;
    }

    public String getSqlDatabase() {
        return sqlDatabase;
    }

    public String getSqlUsername() {
        return sqlUsername;
    }

    public String getSqlPassword() {
        return sqlPassword;
    }

    public int getInitSize() {
        return initSize;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public YamlConfiguration getConf() {
        return conf;
    }
}
