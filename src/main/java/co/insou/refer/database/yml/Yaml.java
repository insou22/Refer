package co.insou.refer.database.yml;

import co.insou.refer.Refer;
import co.insou.refer.database.Database;
import co.insou.refer.database.HistoryPack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Yaml extends Database {

    private Refer refer;

    public Yaml(Refer refer) {
        this.refer = refer;
        init();
    }

    private File databaseFile;
    private YamlConfiguration database;

    private File historyFile;
    private YamlConfiguration history;

    private void init() {
        databaseFile = new File(refer.getDataFolder(), "database.yml");
        if (!refer.getDataFolder().exists()) {
            refer.getDataFolder().mkdirs();
        }
        if (!databaseFile.exists()) {
            try {
                databaseFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(refer);
                refer.disableOnLoad();
            }
            database = new YamlConfiguration();
        } else {
            database = YamlConfiguration.loadConfiguration(databaseFile);
        }
        historyFile = new File(refer.getDataFolder(), "history.yml");
        if (!refer.getDataFolder().exists()) {
            refer.getDataFolder().mkdirs();
        }
        if (!historyFile.exists()) {
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(refer);
                refer.disableOnLoad();
            }
            history = new YamlConfiguration();
            history.createSection("references");
            save(false, true);
        } else {
            history = YamlConfiguration.loadConfiguration(historyFile);
        }
    }

    @Override
    public boolean hasPlayerReferred(OfflinePlayer player) {
        return database.getBoolean(player.getUniqueId().toString() + ".hasReferred");
    }

    @Override
    public void referPlayer(OfflinePlayer referer, OfflinePlayer toRefer) {
        database.set(referer.getUniqueId().toString() + ".hasReferred", true);
        database.set(toRefer.getUniqueId().toString() + ".references", getReferences(toRefer) + 1);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!history.contains("references." + i)) {
                history.set("references." + i + ".referer.uuid", referer.getUniqueId().toString());
                history.set("references." + i + ".referer.name", referer.getName());
                history.set("references." + i + ".referee.uuid", toRefer.getUniqueId().toString());
                history.set("references." + i + ".referee.name", toRefer.getName());
                history.set("references." + i + ".timestamp", System.currentTimeMillis());
                break;
            }
        }
        save(true, true);
    }

    @Override
    public int getReferences(OfflinePlayer player) {
        return database.getInt(player.getUniqueId().toString() + ".references");
    }

    @Override
    public void registerPlayer(OfflinePlayer player) {
        if (!database.contains(player.getUniqueId().toString())) {
            database.set(player.getUniqueId().toString() + ".hasReferred", false);
            database.set(player.getUniqueId().toString() + ".references", 0);
        }
        save(true, false);
    }

    @Override
    public List<HistoryPack> getServerHistory() {
        List<HistoryPack> packs = new ArrayList<>();
        for (String key : history.getConfigurationSection("references").getKeys(false)) {
            Integer id;
            try {
                id = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                refer.getLogger().severe("Refer Error: Malformed history section: key: " + key);
                continue;
            }
            HistoryPack pack = new HistoryPack(
                    id,
                    Bukkit.getOfflinePlayer(UUID.fromString(history.getString("references." + key + ".referer.uuid"))),
                    Bukkit.getOfflinePlayer(UUID.fromString(history.getString("references." + key + ".referee.uuid"))),
                    new Date(history.getLong("references." + key + ".timestamp"))
            );
            packs.add(pack);
        }
        Collections.reverse(packs);
        return packs;
    }

    @Override
    public List<HistoryPack> getHistoryForPlayer(OfflinePlayer player) {
        List<HistoryPack> packs = new ArrayList<>();
        for (String key : history.getConfigurationSection("references").getKeys(false)) {
            Integer id;
            try {
                id = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                System.out.println("Refer Error: Malformed history section: key: " + key);
                continue;
            }
            if (player.getUniqueId().toString().equals(history.getString("references." + key + ".referer.uuid")) || player.getUniqueId().toString().equals(history.getString("references." + key + ".referee.uuid"))) {
                HistoryPack pack = new HistoryPack(
                        id,
                        Bukkit.getOfflinePlayer(UUID.fromString(history.getString("references." + key + ".referer.uuid"))),
                        Bukkit.getOfflinePlayer(UUID.fromString(history.getString("references." + key + ".referee.uuid"))),
                        new Date(history.getLong("references." + key + ".timestamp"))
                );
                packs.add(pack);
            }
        }
        Collections.reverse(packs);
        return packs;
    }

    @Override
    public void deleteHistory(int id) {
        HistoryPack pack = new HistoryPack(
                id,
                Bukkit.getOfflinePlayer(UUID.fromString(history.getString("references." + id + ".referer.uuid"))),
                Bukkit.getOfflinePlayer(UUID.fromString(history.getString("references." + id + ".referee.uuid"))),
                new Date(history.getLong("references." + id + ".timestamp"))
        );
        history.set("references." + String.valueOf(id), null);
        database.set(pack.getReferer().getUniqueId().toString() + ".hasReferred", false);
        database.set(pack.getReferee().getUniqueId().toString() + ".references", getReferences(pack.getReferee()) - 1);
        save(true, true);
    }

    @Override
    public void setReferences(OfflinePlayer player, int amount) {
        database.set(player.getUniqueId().toString() + ".references", amount);
        save(true, false);
    }

    @Override
    public void setCanRefer(OfflinePlayer player, boolean canRefer) {
        database.set(player.getUniqueId().toString() + ".hasReferred", !canRefer);
        save(true, false);
    }

    private synchronized void save(final boolean db, final boolean hist) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (db) {
                        database.save(databaseFile);
                    }
                    if (hist) {
                        history.save(historyFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(refer);
    }
}
