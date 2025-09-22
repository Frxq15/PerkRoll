package me.frxq.perkroll.perk;

import java.util.List;

public class ActivePerk {

    private final Perk perk;
    private final int level;

    public ActivePerk(Perk perk, int level) {
        this.perk = perk;
        this.level = level;
    }
    public Perk getPerk() {
        return perk;
    }
    public int getLevel() {
        return level;
    }
    public List<PerkBoost> getBoosts() {
        return perk.getLevel(level);
    }
}
