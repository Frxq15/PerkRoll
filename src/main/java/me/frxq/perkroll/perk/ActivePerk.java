package me.frxq.perkroll.perk;

import java.util.Collections;
import java.util.List;

public class ActivePerk {
    private final Perk perk;
    private final PerkLevel activeLevel;

    public ActivePerk(Perk perk, PerkLevel activeLevel) {
        this.perk = perk;
        this.activeLevel = activeLevel;
    }

    public Perk getPerk() { return perk; }
    public PerkLevel getActiveLevel() { return activeLevel; }

    public String getDisplay() {
        return activeLevel != null ? activeLevel.getDisplay() : "";
    }

    public List<PerkBoost> getBoosts() {
        return activeLevel != null ? activeLevel.getBoosts() : Collections.emptyList();
    }
}
