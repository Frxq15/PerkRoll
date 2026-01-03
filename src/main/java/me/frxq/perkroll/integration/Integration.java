package me.frxq.perkroll.integration;

import me.frxq.perkroll.PerkRoll;

import java.util.UUID;

public abstract class Integration {
    protected final PerkRoll plugin;

    public Integration(PerkRoll plugin) { this.plugin = plugin; }
    public abstract String getName();
    public abstract IntegrationType getType();
    public abstract void register();
    public abstract void enable();
    public abstract void disable();
    public abstract void reload();
    public abstract boolean isEnabled();
    public abstract double getCurrencyAmount(UUID uuid, String currency);
    public abstract void takeCurrency(UUID uuid, String currency, double amount);
}
