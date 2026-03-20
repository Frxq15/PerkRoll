package me.frxq.perkroll.integration;

import me.frxq.perkroll.perk.PerkType;
import org.bukkit.entity.Player;

public interface BoostProvider {
    void addBooster(Player player, PerkType type, double multiplier);
    void removeBooster(Player player, String boostId);
    void removeAllPerkBoosters(Player player);
    void updateSword(Player player);
}
