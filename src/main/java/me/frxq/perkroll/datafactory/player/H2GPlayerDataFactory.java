package me.frxq.perkroll.datafactory.player;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.h2.H2Handler;
import me.frxq.perkroll.datafactory.sql.SQLHandler;

import java.util.Map;
import java.util.UUID;

public class H2GPlayerDataFactory extends GPlayerDataFactory {
    private PerkRoll plugin;
    private H2Handler h2Handler;

    public H2GPlayerDataFactory(PerkRoll plugin, H2Handler h2Handler) {
        super(plugin);
        this.plugin = plugin;
        this.h2Handler = h2Handler;
    }

    @Override
    public boolean initialize() {
        return false;
    }

    @Override
    public void terminate() {

    }

    @Override
    public void initializeGPlayerData(GPlayer gPlayer) {

    }

    @Override
    public void deleteGPlayerData(UUID uuid) {

    }

    @Override
    public GPlayer getGPlayerData(UUID uuid) {
        return null;
    }

    @Override
    public void updatePlayerName(UUID uuid, String player) {

    }

    @Override
    public void updateGPlayerData(GPlayer gPlayer) {

    }

    @Override
    public boolean doesGPlayerDataExist(UUID uuid) {
        return false;
    }

    @Override
    public boolean isGPlayerDataLoaded(UUID uuid) {
        return false;
    }

    @Override
    public void loadGPlayerData(UUID uuid) {

    }

    @Override
    public void unloadGPlayerData(UUID uuid) {

    }

    @Override
    public String getGPlayerName(UUID uuid) {
        return "";
    }

    @Override
    public UUID getGPlayerUUID(String name) {
        return null;
    }

    @Override
    public Map<UUID, GPlayer> getPlayers() {
        return Map.of();
    }
}
