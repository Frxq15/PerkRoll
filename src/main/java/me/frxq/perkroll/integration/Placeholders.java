package me.frxq.perkroll.integration;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.format.ColorFormatter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {
    private PerkRoll plugin;

    public Placeholders(PerkRoll plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "perkroll";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
    public String onPlaceholderRequest(final Player player, final String identifier) {
        if (player == null) {
            return "Invalid Player";
        }
        GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
        if (gPlayer == null) {
            return "No Data Found";
        }

        switch(identifier.toLowerCase()) {
            case "tickets":
                return String.valueOf(gPlayer.getTickets());
            case "activeperk":
                if(gPlayer.hasActivePerk()) {
                    return ColorFormatter.format(gPlayer.getActivePerk().getDisplay());
                } else {
                    return ColorFormatter.format(plugin.getConfig().getString("placeholders.none-active"));
                }
            default:
                return "Unknown Placeholder";
        }
    }
}
