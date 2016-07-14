package co.insou.refer.database.sql;

import co.insou.refer.Refer;
import co.insou.refer.database.Database;
import co.insou.refer.database.HistoryPack;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SQLManager extends Database {

    private final Refer plugin;
    private ConnectionPoolManager poolManager;

    public SQLManager(Refer plugin) {
        this.plugin = plugin;
        poolManager = new ConnectionPoolManager(plugin);
        init();
    }

    private void init() {
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement referTable = conn.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `Refer` ( " +
                                "UUID VARCHAR(36), " +
                                "HasReferred BOOLEAN, " +
                                "Refers INT, " +
                                "PRIMARY KEY(UUID)" +
                        ");"
                );
                PreparedStatement historyTable = conn.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `History` ( " +
                                "ID INT NOT NULL AUTO_INCREMENT, " +
                                "RefererUUID VARCHAR(36), " +
                                "RefereeUUID VARCHAR(36), " +
                                "Time BIGINT, " +
                                "PRIMARY KEY (ID)" +
                        ");"
                )
        ) {
            referTable.executeUpdate();
            historyTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasPlayerReferred(OfflinePlayer player) {
        long start = System.currentTimeMillis();
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT HasReferred FROM `Refer` WHERE UUID=?"
                )
        ) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
//            System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");
            if (!res.next()) {
                return false;
            }
            return res.getBoolean("HasReferred");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void referPlayer(OfflinePlayer referer, OfflinePlayer toRefer) {
        long start = System.currentTimeMillis();
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE `Refer` SET Refers=? WHERE UUID=?"
                );
                PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO `History` (RefererUUID, RefereeUUID, Time) VALUES (?, ?, ?)"
                );
                PreparedStatement ps3 = conn.prepareStatement(
                        "UPDATE `Refer` SET HasReferred=TRUE WHERE UUID=?"
                )
        ) {
            ps.setInt(1, getReferences(toRefer) + 1);
            ps.setString(2, toRefer.getUniqueId().toString());
            ps.executeUpdate();
            ps3.setString(1, referer.getUniqueId().toString());
            ps3.executeUpdate();
            ps2.setString(1, referer.getUniqueId().toString());
            ps2.setString(2, toRefer.getUniqueId().toString());
            ps2.setLong(3, System.currentTimeMillis());
            ps2.executeUpdate();
//            System.out.println("REFERPLAYER TOOK " + (System.currentTimeMillis() - start));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getReferences(OfflinePlayer player) {
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT Refers FROM `Refer` WHERE UUID=?"
                )
        ) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
            if (!res.next()) {
                return -1;
            }
            return res.getInt("Refers");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void registerPlayer(OfflinePlayer player) {
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT HasReferred FROM `Refer` WHERE UUID=?"
                )
        ) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
            if (!res.next()) {
                PreparedStatement ps2 = conn.prepareStatement(
                        "INSERT INTO `Refer` (UUID, HasReferred, Refers) VALUES (?, FALSE, 0)"
                );
                ps2.setString(1, player.getUniqueId().toString());
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HistoryPack> getServerHistory() {
        List<HistoryPack> packs = new ArrayList<>();
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM `History`"
                )
        ) {
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                HistoryPack pack = new HistoryPack(
                        res.getInt("ID"),
                        Bukkit.getOfflinePlayer(UUID.fromString(res.getString("RefererUUID"))),
                        Bukkit.getOfflinePlayer(UUID.fromString(res.getString("RefereeUUID"))),
                        new Date(res.getLong("Time"))
                );
                packs.add(pack);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packs;
    }

    @Override
    public List<HistoryPack> getHistoryForPlayer(OfflinePlayer player) {
        List<HistoryPack> packs = new ArrayList<>();
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM `History` WHERE RefererUUID=? OR RefereeUUID=?"
                )
        ) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                HistoryPack pack = new HistoryPack(
                        res.getInt("ID"),
                        Bukkit.getOfflinePlayer(UUID.fromString(res.getString("RefererUUID"))),
                        Bukkit.getOfflinePlayer(UUID.fromString(res.getString("RefereeUUID"))),
                        new Date(res.getLong("Time"))
                );
                packs.add(pack);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packs;
    }

    @Override
    public void deleteHistory(int id) {
        try (
            Connection conn = poolManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM `History` WHERE ID=?"
            );
            PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM `History` WHERE ID=?"
            );
            PreparedStatement ps3 = conn.prepareStatement(
                    "UPDATE `Refer` SET HasReferred=FALSE WHERE UUID=?"
            );
            PreparedStatement ps4 = conn.prepareStatement(
                    "UPDATE `Refer` SET Refers=? WHERE UUID=?"
            )
                ) {
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            res.next();
            HistoryPack pack = new HistoryPack(
                    id,
                    Bukkit.getOfflinePlayer(UUID.fromString(res.getString("RefererUUID"))),
                    Bukkit.getOfflinePlayer(UUID.fromString(res.getString("RefereeUUID"))),
                    new Date(res.getLong("Time"))
            );
            ps2.setInt(1, id);
            ps2.executeUpdate();
            ps3.setString(1, pack.getReferer().getUniqueId().toString());
            ps3.executeUpdate();
            ps4.setInt(1, getReferences(pack.getReferee()) - 1);
            ps4.setString(2, pack.getReferee().getUniqueId().toString());
            ps4.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setReferences(OfflinePlayer player, int amount) {
        try (
            Connection conn = poolManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE `Refer` SET Refers=? WHERE UUID=?"
            )
                ) {
            ps.setInt(1, amount);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCanRefer(OfflinePlayer player, boolean canRefer) {
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE `Refer` SET HasReferred=? WHERE UUID=?"
                )
                ) {
            ps.setBoolean(1, !canRefer);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
