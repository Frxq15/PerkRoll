package me.frxq.perkroll.perk;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.format.ColorFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PerkManager {
    private final PerkRoll plugin;
    private final PerkCache cache;

    public PerkManager(PerkRoll plugin) {
        this.plugin = plugin;
        this.cache = plugin.getPerkCache();
    }
    public PerkBoost getRandomPerkBoost() {
        double totalPerkChance = 0;
        for (Perk perk : cache.getAllPerks().values()) {
            totalPerkChance += perk.getChance();
        }

        if (totalPerkChance <= 0) {
            return null;
        }

        double perkRoll = Math.random() * totalPerkChance;
        Perk chosenPerk = null;
        double cumulativePerk = 0;

        for (Perk perk : cache.getAllPerks().values()) {
            cumulativePerk += perk.getChance();
            if (perkRoll <= cumulativePerk) {
                chosenPerk = perk;
                break;
            }
        }

        if (chosenPerk == null) {
            return null;
        }

        double totalLevelChance = 0;
        for (List<PerkBoost> boosts : chosenPerk.getPerks().values()) {
            for (PerkBoost boost : boosts) {
                totalLevelChance += boost.getChance();
            }
        }

        if (totalLevelChance <= 0) {
            return null;
        }

        double levelRoll = Math.random() * totalLevelChance;
        double cumulativeLevel = 0;

        for (List<PerkBoost> boosts : chosenPerk.getPerks().values()) {
            for (PerkBoost boost : boosts) {
                cumulativeLevel += boost.getChance();
                if (levelRoll <= cumulativeLevel) {
                    return boost;
                }
            }
        }
        return null;
    }
    public Perk getPerkByBoost(PerkBoost boost) {
        for (Perk perk : cache.getAllPerks().values()) {
            for (List<PerkBoost> boosts : perk.getPerks().values()) {
                if (boosts.contains(boost)) {
                    return perk;
                }
            }
        }
        return null;
    }
    public void getRandom(Player player) {
        PerkBoost boost = getRandomPerkBoost();
        Bukkit.broadcastMessage(ColorFormatter.format("&b"+player.getName()+" &7rolled the " + boost.getDisplay() + " &7sword perk."));
    }
}
