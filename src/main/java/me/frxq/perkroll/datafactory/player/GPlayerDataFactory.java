package me.frxq.perkroll.datafactory.player;

import me.frxq.perkroll.PerkRoll;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class GPlayerDataFactory {

    protected final PerkRoll plugin;

    public GPlayerDataFactory(PerkRoll plugin) { this.plugin = plugin; }
    public abstract boolean initialize();
    public abstract void terminate();

    public abstract void initializeGPlayerData(GPlayer gPlayer);
    public void initializeGPlayerDataAsync(GPlayer gPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> initializeGPlayerData(gPlayer));
    }

    public abstract void deleteGPlayerData(UUID uuid);
    public void deleteGPlayerDataAsync(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> deleteGPlayerDataAsync(uuid));
    }

    public abstract GPlayer getGPlayerData(UUID uuid);
    public CompletableFuture<GPlayer> getGPlayerDataAsync(UUID uuid) {
        CompletableFuture<GPlayer> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(getGPlayerData(uuid)));
        return future;
    }

    public abstract void updatePlayerName(UUID uuid, String player);

    public abstract void updateGPlayerData(GPlayer gPlayer);
    public void updateGPlayerDataAsync(GPlayer gPlayer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> updateGPlayerData(gPlayer));
    }

    public abstract boolean doesGPlayerDataExist(UUID uuid);
    public CompletableFuture<Boolean> doesGPlayerDataExistAsync(UUID uuid) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(doesGPlayerDataExist(uuid)));
        return future;
    }

    public abstract boolean isGPlayerDataLoaded(UUID uuid);

    public abstract void loadGPlayerData(UUID uuid);
    public void loadGPlayerDataAsync(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> loadGPlayerData(uuid));
    }

    public abstract void unloadGPlayerData(UUID uuid);
    public void unloadGPlayerDataAsync(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> unloadGPlayerData(uuid));
    }
    public abstract String getGPlayerName(UUID uuid);
    public CompletableFuture<String> getGPlayerNameAsync(UUID uuid) {
        CompletableFuture<String> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(getGPlayerName(uuid)));
        return future;
    }
    public abstract UUID getGPlayerUUID(String name);
    public CompletableFuture<UUID> getGPlayerUUIDAsync(String name) {
        CompletableFuture<UUID> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> future.complete(getGPlayerUUID(name)));
        return future;
    }
    public abstract Map<UUID, GPlayer> getPlayers();
}

