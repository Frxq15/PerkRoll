package me.frxq.perkroll.perk;

public class PerkBoost {
    private final String boostType;    // "currency", "damage"
    private final String boostTarget;  // "Shard", "Resistance"
    private final double amount;       // 5, 10, 15

    public PerkBoost(String boostType, String boostTarget, double amount) {
        this.boostType = boostType;
        this.boostTarget = boostTarget;
        this.amount = amount;
    }

    public String getBoostType() { return boostType; }
    public String getBoostTarget() { return boostTarget; }
    public double getAmount() { return amount; }
}
