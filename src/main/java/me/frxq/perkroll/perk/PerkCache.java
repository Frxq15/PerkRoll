package me.frxq.perkroll.perk;

import me.frxq.perkroll.PerkRoll;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PerkCache {
    private PerkRoll plugin;
    private HashMap<String, Perk> cache = new HashMap<>();

    private FileConfiguration file;

    public PerkCache(PerkRoll plugin) {
        this.plugin = plugin;
        this.file = plugin.getConfig();
        cachePerks();
    }
    public void cachePerks() {
        cache.clear();

        ConfigurationSection perksSection = file.getConfigurationSection("perks");
        if (perksSection == null) return;

        for (String perkName : perksSection.getKeys(false)) {
            ConfigurationSection perkSection = perksSection.getConfigurationSection(perkName);

            double rarity = perkSection.getDouble("chance", 10);
            HashMap<Integer, List<PerkBoost>> boostsByLevel = new HashMap<>();

            ConfigurationSection levelsSection = perkSection.getConfigurationSection("levels");
            if (levelsSection != null) {
                for (String levelKey : levelsSection.getKeys(false)) {
                    int level = Integer.parseInt(levelKey);
                    ConfigurationSection levelSection = levelsSection.getConfigurationSection(levelKey);

                    String display = levelSection.getString("display", "Unknown");
                    double chance = Double.parseDouble(levelSection.getString("chance", "0"));

                    List<PerkBoost> perkBoosts = new ArrayList<>();
                    ConfigurationSection boostsSection = levelSection.getConfigurationSection("boosts");

                    if (boostsSection != null) {
                        for (String boostKey : boostsSection.getKeys(false)) {
                            ConfigurationSection boostSection = boostsSection.getConfigurationSection(boostKey);

                            String boostType = boostSection.getString("type", "unknown");
                            String boostTarget = boostSection.getString("boost", "");
                            double amount = Double.parseDouble(boostSection.getString("amount", "0"));

                            perkBoosts.add(new PerkBoost(level, display, boostType, boostTarget, amount, chance));
                        }
                    }

                    boostsByLevel.put(level, perkBoosts);
                }
            }
            Perk perk = new Perk(perkName, rarity, boostsByLevel);
            cache.put(perkName, perk);
        }
    }

    public Perk getPerk(String name) {
        return cache.get(name);
    }
    public String debugAllPerks() {
        StringBuilder sb = new StringBuilder();
        for (String perkName : cache.keySet()) {
            Perk perk = cache.get(perkName);
            sb.append("Perk: ").append(perk.getName()).append(", Chance: ").append(perk.getChance()).append("\n");
            for (int level : perk.getPerks().keySet()) {
                sb.append("  Level ").append(level).append(":\n");
                for (PerkBoost boost : perk.getLevel(level)) {
                    sb.append("    - Type: ").append(boost.getBoostType())
                      .append(", Target: ").append(boost.getBoostTarget())
                      .append(", Amount: ").append(boost.getAmount())
                      .append(", Chance: ").append(boost.getChance()).append("\n");
                }
            }
        }
        return sb.toString();
    }

    public HashMap<String, Perk> getAllPerks() {
        return cache;
    }
}
