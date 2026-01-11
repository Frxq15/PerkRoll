package me.frxq.perkroll.integration.integrations;

import me.clip.placeholderapi.PlaceholderAPI;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.Integration;
import me.frxq.perkroll.integration.IntegrationType;
import me.frxq.perkroll.integration.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class PlaceholderAPIIntegration extends Integration {
    private boolean isEnabled;
    private Placeholders placeholders;
    public PlaceholderAPIIntegration(PerkRoll plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "PlaceholderAPI";
    }

    @Override
    public IntegrationType getType() {
        return IntegrationType.PLACEHOLDERAPI;
    }

    @Override
    public void register() {
        isEnabled = false;
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            isEnabled = true;
            plugin.log("Integrations: Enabling placeholders..");
            placeholders = new Placeholders(plugin);
            placeholders.register();
            return;
        }
        plugin.warn("Integrations: Failed to enable " + getName() + " integration, jar could not be found");
        isEnabled = false;
    }

    @Override
    public void disable() {
        placeholders.unregister();
        isEnabled = false;
    }

    @Override
    public void reload() {
        enable();
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public double getCurrencyAmount(UUID uuid, String currency) {
        return 0;
    }

    @Override
    public void takeCurrency(UUID uuid, String currency, double amount) {

    }

    public String applyPlaceholders(OfflinePlayer target, String message) {
        return PlaceholderAPI.setPlaceholders(target, message);
    }
}
