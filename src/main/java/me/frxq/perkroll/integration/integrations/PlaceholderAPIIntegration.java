package me.frxq.perkroll.integration.integrations;

import me.clip.placeholderapi.PlaceholderAPI;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.Integration;
import me.frxq.perkroll.integration.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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
    public boolean isRequired() {
        return false;
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            isEnabled = true;
            plugin.log("Integrations: Enabled " + getName() + " integration");
            placeholders = new Placeholders(plugin);
            placeholders.register();
            return;
        }
        plugin.warn("Integrations: " + getName() + " not found, skipping");
        isEnabled = false;
    }

    @Override
    public void disable() {
        if (placeholders != null) {
            placeholders.unregister();
        }
        isEnabled = false;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public String applyPlaceholders(OfflinePlayer target, String message) {
        return PlaceholderAPI.setPlaceholders(target, message);
    }
}
