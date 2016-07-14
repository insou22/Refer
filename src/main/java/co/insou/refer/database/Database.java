package co.insou.refer.database;

import org.bukkit.OfflinePlayer;

import java.util.List;

public abstract class Database {

    public abstract boolean hasPlayerReferred(OfflinePlayer player);

    public abstract void referPlayer(OfflinePlayer referer, OfflinePlayer toRefer);

    public abstract int getReferences(OfflinePlayer player);

    public abstract void registerPlayer(OfflinePlayer player);

    public abstract List<HistoryPack> getServerHistory();

    public abstract List<HistoryPack> getHistoryForPlayer(OfflinePlayer player);

    public abstract void deleteHistory(int id);

    public abstract void setReferences(OfflinePlayer player, int amount);

    public abstract void setCanRefer(OfflinePlayer player, boolean canRefer);

}
