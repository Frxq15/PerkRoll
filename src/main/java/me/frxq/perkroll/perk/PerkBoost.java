package me.frxq.perkroll.perk;

public class PerkBoost {
    private final int level;
    private final String boostType;
    private final String boostTarget;
    private final double amount;
    private final double chance;

    public PerkBoost(int level, String boostType, String boostTarget, double amount, double chance) {
        this.level = level;
        this.boostType = boostType;
        this.boostTarget = boostTarget;
        this.amount = amount;
        this.chance = chance;
    }

    public int getLevel() { return level; }
    public String getBoostType() { return boostType; }
    public String getBoostTarget() { return boostTarget; }
    public double getAmount() { return amount; }
    public double getChance() { return chance; }
}

