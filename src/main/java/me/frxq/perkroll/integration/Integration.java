package me.frxq.perkroll.integration;

import me.frxq.perkroll.PerkRoll;

public abstract class Integration {
    protected final PerkRoll plugin;

    public Integration(PerkRoll plugin) { this.plugin = plugin; }

    public abstract String getName();
    public abstract boolean isRequired();
    public abstract void enable();
    public abstract void disable();
    public abstract boolean isEnabled();
}
