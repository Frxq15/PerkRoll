package me.frxq.perkroll.integration.integrations;

import es.edwardbelt.eddungeons.iapi.EdDungeonsAPI;
import es.edwardbelt.eddungeons.iapi.EdDungeonsBoostersAPI;
import es.edwardbelt.eddungeons.iapi.EdDungeonsCurrencyAPI;
import es.edwardbelt.eddungeons.iapi.EdDungeonsSwordAPI;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.Integration;
import me.frxq.perkroll.integration.IntegrationType;
import org.bukkit.Bukkit;

import java.util.UUID;

public class EdDungeonsIntegration extends Integration {
    private boolean isEnabled;

    public EdDungeonsIntegration(PerkRoll plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "EdDungeons";
    }

    @Override
    public IntegrationType getType() {
        return IntegrationType.EDDUNGEONS;
    }

    @Override
    public void register() {
        enable();
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("EdDungeons") != null) {
            isEnabled = true;
            plugin.log("Integrations: Enabled " + getName() + " integration");
            return;
        }
        plugin.warn("Integrations: Failed to enable " + getName() + " integration, jar could not be found");
        isEnabled = false;
    }

    @Override
    public void disable() {
        isEnabled = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public EdDungeonsCurrencyAPI getCurrencyAPI() {
        return EdDungeonsAPI.getInstance().getCurrencyAPI();
    }
    public EdDungeonsBoostersAPI getBoosterAPI() {
        return EdDungeonsAPI.getInstance().getBoostersAPI();
    }
    public EdDungeonsSwordAPI getSwordAPI() {
        return EdDungeonsAPI.getInstance().getSwordAPI();
    }
}

