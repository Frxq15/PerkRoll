package me.frxq.perkroll.perk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Perk {
    private final String name;
    private final double chance;
    private final HashMap<Integer, List<PerkBoost>> perks;

    public Perk(String name, double chance, HashMap<Integer, List<PerkBoost>> perks) {
        this.name = name;
        this.chance = chance;
        this.perks = perks;
    }

    public String getName() { return name; }
    public double getChance() { return chance; }
    public HashMap<Integer, List<PerkBoost>> getPerks() { return perks; }

    public List<PerkBoost> getLevel(int level) {
        return perks.getOrDefault(level, new ArrayList<>());
    }

}
