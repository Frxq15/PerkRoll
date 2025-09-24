package me.frxq.perkroll.datafactory;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.h2.H2Handler;
import me.frxq.perkroll.datafactory.player.GPlayerDataFactory;
import me.frxq.perkroll.datafactory.player.H2GPlayerDataFactory;
import me.frxq.perkroll.datafactory.player.SQLGPlayerDataFactory;
import me.frxq.perkroll.datafactory.sql.SQLHandler;
import org.bukkit.Bukkit;

public class DataFactory {
    private PerkRoll plugin;
    private String host, database, username, password;
    private int port;
    private StorageType storageType;
    private SQLHandler sqlHandler;
    private H2Handler h2Handler;

    private GPlayerDataFactory playerDataFactory;

    public DataFactory(PerkRoll plugin) {
        this.plugin = plugin;
    }
    public boolean initialize() {
        assignStorageMethod();
        switch (storageType) {
            case MYSQL:
                setSqlDetails();
                sqlHandler = new SQLHandler(plugin, getHost(), getDatabase(), getUsername(), getPassword(), getPort());
                if(!setupSQL()) {
                    plugin.error("Data Factory: Could not connect to MySQL database `" + getDatabase() + "`. Disabling plugin.");
                    return false;
                }
                plugin.log("Data Factory: Connected to MySQL database `" + getDatabase() + "` successfully.");
                return true;
            case H2:
                h2Handler = new H2Handler(plugin);
                if (!setupH2()) {
                    plugin.error("Data Factory: Could not connect to H2 database. Disabling plugin.");
                    return false;
                }
                plugin.log("Data Factory: Connected to H2 database successfully.");
                return true;
        }
        return false;
    }

    public void terminate() {
        if(playerDataFactory != null) playerDataFactory.terminate();
    }

    public void assignStorageMethod() {
        String storageMethod = plugin.getConfig().getString("storage-method");
        if (storageMethod != null) {
            try {
                storageType = StorageType.valueOf(storageMethod.toUpperCase());
            } catch (IllegalArgumentException e) {
                plugin.warn("Data Factory: Invalid storage method specified in config. Defaulting to H2.");
                storageType = StorageType.H2;
            }
        } else {
            plugin.log("Data Factory: No storage method found in config. Defaulting to H2.");
            storageType = StorageType.H2;
        }
    }
    public void setSqlDetails() {
        host = plugin.getConfig().getString("database.mysql.host");
        database = plugin.getConfig().getString("database.mysql.database");
        username = plugin.getConfig().getString("database.mysql.username");
        password = plugin.getConfig().getString("database.mysql.password");
        port = plugin.getConfig().getInt("database.mysql.port");
    }
    public String getHost() {
        return host;
    }
    public String getDatabase() {
        return database;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getPort() {
        return port;
    }

    public boolean setupSQL() {
        if (!sqlHandler.connect()) {
            plugin.error("Data Factory: MySQL connection failed, please ensure your connection details are correct inside of the config.yml file.");
            return false;
        }
        playerDataFactory = new SQLGPlayerDataFactory(plugin, sqlHandler);
        playerDataFactory.initialize();
        Bukkit.getPluginManager().registerEvents(new DataFactoryListeners(plugin), plugin);
        return true;
    }

    public boolean setupH2() {
        if (!h2Handler.connect()) {
            plugin.error("Data Factory: H2 connection failed.");
            return false;
        }
        playerDataFactory = new H2GPlayerDataFactory(plugin, h2Handler);
        playerDataFactory.initialize();
        Bukkit.getPluginManager().registerEvents(new DataFactoryListeners(plugin), plugin);
        return true;
    }
    public GPlayerDataFactory getGPlayerDataFactory() {
        return playerDataFactory;
    }
}
