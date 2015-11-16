package co.insou.refer.database;

import org.bukkit.entity.Player;

/**
 * Created by Zac on 16/11/2015.
 */
public abstract class Database {

    public abstract boolean hasPlayerReferred(Player player);

    public abstract void referPlayer(Player referer, Player toRefer);

    public abstract int getReferences(Player player);

    public abstract void registerPlayer(Player player);

}
