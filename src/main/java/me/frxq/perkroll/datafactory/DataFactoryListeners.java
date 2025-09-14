package me.frxq.perkroll.datafactory;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class DataFactoryListeners implements Listener {
    private final PerkRoll plugin;

    public DataFactoryListeners(PerkRoll plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        String name = event.getName();

        GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(uuid);
        if (gPlayer == null) {
            if (plugin.getDataFactory().getGPlayerDataFactory().doesGPlayerDataExist(uuid)) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage("An error occurred while loading your data, please contact an administrator.");
                return;
            }
            gPlayer = new GPlayer(plugin, uuid, name);
            plugin.getDataFactory().getGPlayerDataFactory().initializeGPlayerData(gPlayer);
            plugin.getDataFactory().getGPlayerDataFactory().updatePlayerName(uuid, name);
        } else {
            gPlayer.setName(name);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
        if (gPlayer == null) {
            plugin.error("Data Factory: GPlayer data for " + player.getName() + " is null on quit event. This should not happen.");
            return;
        }
        plugin.getDataFactory().getGPlayerDataFactory().unloadGPlayerDataAsync(player.getUniqueId());
    }
}
