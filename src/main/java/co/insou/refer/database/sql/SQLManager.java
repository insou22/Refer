package co.insou.refer.database.sql;

import co.insou.refer.Refer;
import co.insou.refer.database.Database;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Zac on 16/11/2015.
 */
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
                PreparedStatement ps = conn.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `Refer` ( " +
                                "UUID VARCHAR(30), " +
                                "HasReferred BOOLEAN, " +
                                "Refers INT, " +
                                "PRIMARY KEY(UUID)" +
                                ");"
                )
        ) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasPlayerReferred(Player player) {
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT HasReferred FROM `Refer` WHERE UUID=?"
                )
        ) {
            ps.setString(1, player.getUniqueId().toString());
            ResultSet res = ps.executeQuery();
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
    public void referPlayer(Player referer, Player toRefer) {
        try (
            Connection conn = poolManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE `Refer` SET HasReferred=TRUE WHERE UUID=?"
            );
            PreparedStatement ps2 = conn.prepareStatement(
                    "UPDATE `Refer` SET Refers=? WHERE UUID=?"
            );
        ) {
            ps.setString(1, referer.getUniqueId().toString());
            ps.executeUpdate();
            ps2.setString(2, toRefer.getUniqueId().toString());
            ps2.setInt(1, getReferences(toRefer) + 1);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getReferences(Player player) {
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT Refers FROM `Refer` WHERE UUID=?"
                );
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
    public void registerPlayer(Player player) {
        try (
                Connection conn = poolManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT HasReferred FROM `Refer` WHERE UUID=?"
                );
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
            player.kickPlayer("Refer Error: Unable to register player!");
            e.printStackTrace();
        }
    }
}
