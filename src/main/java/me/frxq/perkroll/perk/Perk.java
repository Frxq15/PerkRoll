package me.frxq.perkroll.perk;

import java.util.HashMap;
import java.util.List;

public class Perk {
    private final String name;
    private final String rarity;
    private final HashMap<Integer, List<PerkBoost>> perks;

    public Perk(String name, String rarity, HashMap<Integer, List<PerkBoost>> perks) {
        this.name = name;
        this.rarity = rarity;
        this.perks = perks;
    }

    public String getName() { return name; }
    public String getRarity() { return rarity; }
    public HashMap<Integer, List<PerkBoost>> getPerks() { return perks; }
}
