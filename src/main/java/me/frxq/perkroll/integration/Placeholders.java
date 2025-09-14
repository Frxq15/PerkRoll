package me.frxq.perkroll.integration;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.frxq.perkroll.PerkRoll;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {
    private PerkRoll plugin;

    public Placeholders(PerkRoll plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "PerkRoll";
    }

    @Override
    public @NotNull String getAuthor() {
        return "";
    }

    @Override
    public @NotNull String getVersion() {
        return "";
    }
}
