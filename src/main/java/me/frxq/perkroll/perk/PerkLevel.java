package me.frxq.perkroll.perk;

import java.util.List;

public class PerkLevel {
    private final int level;
    private final double chance;
    private final String display;
    private final List<PerkBoost> boosts;

    public PerkLevel(int level, double chance, String display, List<PerkBoost> boosts) {
        this.level = level;
        this.chance = chance;
        this.display = display;
        this.boosts = boosts;
    }

    public int getLevel() { return level; }
    public double getChance() { return chance; }
    public String getDisplay() { return display; }
    public List<PerkBoost> getBoosts() { return boosts; }
}
