package co.insou.refer.database;

import org.bukkit.OfflinePlayer;

import java.util.Date;

/**
 * Created by insou on 13/02/2016.
 */
public class HistoryPack {

    private int id;
    private OfflinePlayer referer;
    private OfflinePlayer referee;
    private Date timestamp;

    public HistoryPack(int id, OfflinePlayer referer, OfflinePlayer referee, Date timestamp) {
        this.id = id;
        this.referer = referer;
        this.referee = referee;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public OfflinePlayer getReferer() {
        return referer;
    }

    public OfflinePlayer getReferee() {
        return referee;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
