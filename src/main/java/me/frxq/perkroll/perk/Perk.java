package me.frxq.perkroll.perk;

import java.util.HashMap;

public class Perk {
    private final String name;
    private final double baseChance;
    private final HashMap<Integer, PerkLevel> levels;

    public Perk(String name, double baseChance, HashMap<Integer, PerkLevel> levels) {
        this.name = name;
        this.baseChance = baseChance;
        this.levels = levels;
    }

    public String getName() { return name; }
    public double getBaseChance() { return baseChance; }
    public HashMap<Integer, PerkLevel> getLevels() { return levels; }

    public PerkLevel getLevel(int level) {
        return levels.get(level);
    }
    public int getMaxLevel() { return levels.size(); }
}
