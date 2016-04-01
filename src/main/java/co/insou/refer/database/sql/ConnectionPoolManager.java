package co.insou.refer.database.sql;

import co.insou.refer.Refer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPoolManager {

    private HikariDataSource ds;

    private String SQL_URL;
    private String username;
    private String password;

    private int minConnections;
    private int maxConnections;
    private long connectionTimeout;

    private Refer plugin;

    protected ConnectionPoolManager(Refer plugin) {
        this.plugin = plugin;
        setSQLURL(
                "jdbc:mysql://" + plugin.getReferConfig().getSqlHostname() + ":" + plugin.getReferConfig().getSqlPort() + "/" + plugin.getReferConfig().getSqlDatabase(),
                plugin.getReferConfig().getSqlUsername(),
                plugin.getReferConfig().getSqlPassword()
        );
        setConnectionPoolOptions(
                plugin.getReferConfig().getInitSize(),
                plugin.getReferConfig().getMaxActive(),
                plugin.getReferConfig().getMaxWait()
        );
        setupDataSource();
    }

    public void setupDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(SQL_URL);
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        System.out.println("Setting min conns to " + minConnections + ", max: " + maxConnections);
        config.setMinimumIdle(minConnections);
        config.setMaximumPoolSize(maxConnections);
        System.out.println("Timeout millis: " + connectionTimeout);
        config.setConnectionTimeout(connectionTimeout);
        ds = new HikariDataSource(config);
        ds.resumePool();
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public void setSQLURL(String SQLURL, String username, String password) {
        this.SQL_URL = SQLURL;
        this.username = username;
        this.password = password;
    }

    public void setConnectionPoolOptions(int minConnections, int maxConnections, long connectionTimeout) {
        this.minConnections = minConnections;
        this.maxConnections = maxConnections;
        this.connectionTimeout = connectionTimeout;
    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try {
            conn.close();
        } catch (SQLException ignored) {
        }
        if (ps != null) try {
            ps.close();
        } catch (SQLException ignored) {
        }
        if (res != null) try {
            res.close();
        } catch (SQLException ignored) {
        }
    }

}
