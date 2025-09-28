package me.frxq.perkroll.datafactory.player;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.h2.H2Handler;
import me.frxq.perkroll.datafactory.sql.SQLHandler;
import me.frxq.perkroll.perk.ActivePerk;
import me.frxq.perkroll.perk.Perk;
import me.frxq.perkroll.perk.PerkLevel;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class H2GPlayerDataFactory extends GPlayerDataFactory {
    private H2Handler h2Handler;

    private final String PLAYERS_TABLE = "perkroll_players";
    private int savingTask;

    private final static Map<UUID, GPlayer> players = new ConcurrentHashMap<>();

    public H2GPlayerDataFactory(PerkRoll plugin, H2Handler h2Handler) {
        super(plugin);
        this.h2Handler = h2Handler;
    }

    @Override
    public boolean initialize() {
        if (!connected()) {
            return false;
        }
        try (PreparedStatement statement = h2Handler.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + PLAYERS_TABLE + " " +
                "(uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(16), active VARCHAR(128) NULL, active_level INT, tickets INT, tickets_used INT, " +
                "till_guaranteed INT);")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        plugin.log("Data Factory: GPlayerDataFactory initialized successfully.");
        savingTask = startSavingTask();
        return true;
    }

    @Override
    public void terminate() {
        Bukkit.getScheduler().cancelTask(savingTask);
        savingTask = 0;
        if (!connected()) {
            return;
        }
        List<GPlayer> save = new ArrayList<>(players.values());
        AtomicInteger saveCount = new AtomicInteger();
        save.forEach(g -> {
            unloadGPlayerData(g.getUUID());
            saveCount.getAndIncrement();
        });
        players.clear();
        plugin.log("Data Factory: Saved "+saveCount.get() + " players to the database.");
        plugin.log("Data Factory: H2GPlayerDataFactory terminated successfully.");
    }

    @Override
    public void initializeGPlayerData(GPlayer gPlayer) {
        if (!connected()) {
            return;
        }
        if (doesGPlayerDataExist(gPlayer.getUUID())) {
            return;
        }
        String perkName = null;
        int perkLevel = 0;

        if (gPlayer.getActivePerk() != null) {
            if (gPlayer.getActivePerk().getPerk() != null) {
                perkName = gPlayer.getActivePerk().getPerk().getName();
            }
            perkLevel = gPlayer.getActivePerk().getActiveLevel().getLevel();
        }
        try (PreparedStatement statement = h2Handler.getConnection().prepareStatement("INSERT INTO " + PLAYERS_TABLE + " " +
                "(uuid, name, active, active_level, tickets, tickets_used, till_guaranteed) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);")) {
            statement.setString(1, (gPlayer.getUUID() == null ? null : gPlayer.getUUID().toString()));
            statement.setString(2, gPlayer.getName());
            statement.setString(3, perkName);
            statement.setInt(4, perkLevel);
            statement.setInt(5, gPlayer.getTickets());
            statement.setInt(6, gPlayer.getTicketsUsed());
            statement.setInt(7, gPlayer.getTillGuaranteed());
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.error("Data Factory: An error occurred while initializing player " + gPlayer.getName());
            e.printStackTrace();
        }
        if (!players.containsKey(gPlayer.getUUID())) {
            players.put(gPlayer.getUUID(), gPlayer);
        }
    }

    @Override
    public void deleteGPlayerData(UUID uuid) {
        if (!connected()) {
            return;
        }
        if (doesGPlayerDataExist(uuid)) {
            return;
        }
        try (PreparedStatement statement = h2Handler.getConnection().prepareStatement("DELETE FROM " + PLAYERS_TABLE + " WHERE uuid=?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.error("Data Factory: An error occurred while deleting player " + uuid);
            e.printStackTrace();
        }
        players.remove(uuid);
    }

    @Override
    public GPlayer getGPlayerData(UUID uuid) {
        if(uuid == null) {
            return null;
        }
        if(players.get(uuid) != null) {
            return players.get(uuid);
        }
        if (!connected()) {
            return null;
        }
        try (PreparedStatement statement = h2Handler.getConnection().prepareStatement("SELECT * FROM " + PLAYERS_TABLE + " WHERE uuid=?")) {
            statement.setString(1, uuid.toString());

            ResultSet rs = statement.executeQuery();
            GPlayer gPlayer = null;

            if (rs.next()) {
                UUID uuidDB = (rs.getString("uuid") == null ? null : UUID.fromString(rs.getString("uuid")));
                String name = rs.getString("name");
                String active = rs.getString("active");
                int active_level = rs.getInt("active_level");
                int tickets = rs.getInt("tickets");
                int tickets_used = rs.getInt("tickets_used");
                int till_guaranteed = rs.getInt("till_guaranteed");

                ActivePerk activePerk = null;
                if (active != null) {
                    Perk perk = plugin.getPerkCache().getPerk(active);
                    PerkLevel level = perk != null ? perk.getLevel(active_level) : null;

                    if (perk != null && level != null) {
                        activePerk = new ActivePerk(perk, level);
                    }
                }

                gPlayer = new GPlayer(plugin, uuidDB, name, tickets, till_guaranteed, tickets_used);
                if(activePerk != null) gPlayer.setActivePerk(activePerk);

                if (!players.containsKey(gPlayer.getUUID())) {
                    players.put(gPlayer.getUUID(), gPlayer);
                }
            }

            rs.close();
            return gPlayer;
        } catch (SQLException e) {
            plugin.error("Data Factory: An error occurred while getting player " + uuid);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updatePlayerName(UUID uuid, String player) {
        if (!connected()) {
            return;
        }
        try {
            PreparedStatement selectPlayer = h2Handler.getConnection().prepareStatement("SELECT * FROM " + PLAYERS_TABLE
                    + " WHERE uuid = ?;");
            selectPlayer.setString(1, uuid.toString());
            ResultSet playerResult = selectPlayer.executeQuery();

            if (playerResult.next() && !playerResult.getString("name").equals(player)) {
                PreparedStatement updateName = h2Handler.getConnection().prepareStatement("UPDATE "+PLAYERS_TABLE +
                        " SET name = ? WHERE uuid = ?;");
                updateName.setString(1, player);
                updateName.setString(2, uuid.toString());
                updateName.executeUpdate();
            }
            playerResult.close();
        } catch (SQLException e) {
            plugin.error("Data Factory: Failed to update player name for " + uuid);
            e.printStackTrace();
        }
    }

    @Override
    public void updateGPlayerData(GPlayer gPlayer) {
        if (!connected()) {
            return;
        }
        final String UPDATE_DATA =
                "MERGE INTO " + PLAYERS_TABLE + " (uuid, name, active, active_level, tickets, tickets_used, till_guaranteed) " +
                        "KEY (uuid) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement statement = h2Handler.getConnection().prepareStatement(UPDATE_DATA)) {
            int i = 1;

            UUID uuid = gPlayer.getUUID();
            String name = gPlayer.getName();

            String active = null;
            int active_level = 0;

            if (gPlayer.getActivePerk() != null && gPlayer.getActivePerk().getPerk() != null) {
                active = gPlayer.getActivePerk().getPerk().getName();
                active_level = gPlayer.getActivePerk().getActiveLevel().getLevel();
            }

            int tickets = gPlayer.getTickets();
            int tickets_used = gPlayer.getTicketsUsed();
            int till_guaranteed = gPlayer.getTillGuaranteed();

            statement.setString(i++, (uuid == null ? null : uuid.toString()));
            statement.setString(i++, name);
            statement.setString(i++, active);
            statement.setInt(i++, active_level);
            statement.setInt(i++, tickets);
            statement.setInt(i++, tickets_used);
            statement.setInt(i, till_guaranteed);

            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.error("Data Factory: An error occurred while updating player " + gPlayer.getUUID());
            e.printStackTrace();
        }
    }

    @Override
    public boolean doesGPlayerDataExist(UUID uuid) {
        if(!connected()) {
            return false;
        }
        try (PreparedStatement statement = h2Handler.getConnection().prepareStatement("SELECT uuid FROM " + PLAYERS_TABLE + " where uuid=?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            boolean exists = rs.next();
            rs.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isGPlayerDataLoaded(UUID uuid) {
        return players.containsKey(uuid);
    }

    @Override
    public void loadGPlayerData(UUID uuid) {
        getGPlayerData(uuid);
    }

    @Override
    public void unloadGPlayerData(UUID uuid) {
        if (players.get(uuid) != null) {
            updateGPlayerData(players.remove(uuid));
        }
    }

    @Override
    public String getGPlayerName(UUID uuid) {
        if(!connected()) {
            return null;
        }
        try (PreparedStatement statement = h2Handler.getConnection().prepareStatement("SELECT name FROM " + PLAYERS_TABLE + " WHERE uuid=?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            String name = null;
            if (rs.next()) {
                name = rs.getString("name");
            }
            rs.close();
            return name;
        } catch (SQLException e) {
            plugin.error("Data Factory: An error occurred while getting player name for " + uuid);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UUID getGPlayerUUID(String name) {
        if(!connected()) {
            return null;
        }
        try (PreparedStatement statement = h2Handler.getConnection().prepareStatement("SELECT uuid FROM `" + PLAYERS_TABLE + "` WHERE name=?")) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            UUID uuid = null;
            if (rs.next()) {
                uuid = UUID.fromString(rs.getString("uuid"));
            }
            rs.close();
            return uuid;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<UUID, GPlayer> getPlayers() {
        return players;
    }

    private int startSavingTask() {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                    List<GPlayer> snapshot;
                    synchronized (players) {
                        snapshot = new ArrayList<>(players.values());
                    }
                    for (GPlayer gPlayer : snapshot) {
                        updateGPlayerData(gPlayer);
                    }
                }, 20L * 60L * 10,
                20L * 60L * 10).getTaskId();
    }

    public boolean connected() {
        if (!h2Handler.isConnected()) {
            if (!h2Handler.connect()) {
                plugin.error("Data Factory: Can't establish a database connection!");
                return false;
            }
        }
        return true;
    }
}
