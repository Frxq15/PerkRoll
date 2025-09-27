package me.frxq.perkroll.perk;

public class PerkBoost {
    private final PerkType boostType;    // "currency", "damage"
    private final String boostTarget;  // "Shard", "Resistance"
    private final double amount;       // 5, 10, 15

    public PerkBoost(PerkType boostType, String boostTarget, double amount) {
        this.boostType = boostType;
        this.boostTarget = boostTarget;
        this.amount = amount;
    }

    public PerkType getBoostType() { return boostType; }
    public String getBoostTarget() { return boostTarget; }
    public double getAmount() { return amount; }
}
