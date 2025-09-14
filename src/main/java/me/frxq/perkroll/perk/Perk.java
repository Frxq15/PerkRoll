package me.frxq.perkroll.perk;

public class Perk {

    private final String name;
    private final String rarity;
    private final int level;

    public Perk(String name, String rarity, int level) {
        this.name = name;
        this.rarity = rarity;
        this.level = level;
    }

    public String getName() {
        return name;
    }
    public String getRarity() {
        return rarity;
    }
    public int getLevel() { return level; }
}
