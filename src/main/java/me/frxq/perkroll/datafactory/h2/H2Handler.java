package me.frxq.perkroll.datafactory.h2;

import me.frxq.perkroll.PerkRoll;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Handler {
    private PerkRoll plugin;
    private Connection connection;
    private final String dbPath;

    public H2Handler(PerkRoll plugin) {
        this.plugin = plugin;
        this.dbPath = plugin.getDataFolder().getAbsolutePath() + "/database";

        File dbFile = new File(plugin.getDataFolder(), "database.mv.db");
        if (!dbFile.exists()) {
            try {
                dbFile.getParentFile().mkdirs();
                dbFile.createNewFile();
            } catch (IOException e) {
                plugin.error("Failed to create H2 database file!");
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean connect() {
        try {
            if (connection != null && !connection.isClosed()) return true;

            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:" + dbPath);
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            connection = null;
            plugin.error("H2 connection failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void disconnect() {
        if (connection == null) return;

        try {
            connection.close();
        } catch (SQLException e) {
            plugin.error(e.getMessage());
        }
    }

    public synchronized boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public synchronized Connection getConnection() {
        if (!isConnected()) {
            connect();
        }
        return connection;
    }
}
