package me.frxq.perkroll.perk;

import me.frxq.perkroll.PerkRoll;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerkCache {
    private PerkRoll plugin;
    private Map<String, Perk> cache = new HashMap<>();

    private FileConfiguration config;

    public PerkCache(PerkRoll plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        cachePerks();
    }
    public void cachePerks() {
        cache.clear();

        ConfigurationSection perksSection = config.getConfigurationSection("perks");
        if (perksSection == null) return;

        for (String perkName : perksSection.getKeys(false)) {
            ConfigurationSection perkSection = perksSection.getConfigurationSection(perkName);

            double baseChance = Double.parseDouble(perkSection.getString("chance", "0"));
            HashMap<Integer, PerkLevel> levels = new HashMap<>();

            ConfigurationSection levelsSection = perkSection.getConfigurationSection("levels");
            if (levelsSection != null) {
                for (String levelKey : levelsSection.getKeys(false)) {
                    int level = Integer.parseInt(levelKey);
                    ConfigurationSection levelSection = levelsSection.getConfigurationSection(levelKey);

                    double levelChance = Double.parseDouble(levelSection.getString("chance", "0"));
                    String display = levelSection.getString("display", "");

                    List<PerkBoost> boosts = new ArrayList<>();
                    ConfigurationSection boostsSection = levelSection.getConfigurationSection("boosts");
                    if (boostsSection != null) {
                        for (String boostKey : boostsSection.getKeys(false)) {
                            ConfigurationSection boostSection = boostsSection.getConfigurationSection(boostKey);

                            String type = boostSection.getString("type", "invalid");
                            PerkType perkType;
                            try {
                                perkType = PerkType.valueOf(type.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                plugin.warn("Cache: Skipping.. Invalid perk type '" + type + "' in perk '" + perkName + "', level " + level);
                                continue;
                            }
                            String boostTarget = boostSection.getString("boost", "");
                            double amount = Double.parseDouble(boostSection.getString("amount", "0"));

                            boosts.add(new PerkBoost(perkType, boostTarget, amount));
                        }
                    }

                    levels.put(level, new PerkLevel(level, levelChance, display, boosts));
                }
            }
            cache.put(perkName, new Perk(perkName, baseChance, levels));
        }
    }

    public Perk getPerk(String name) {
        return cache.get(name);
    }
    public String debugAllPerks() {
        StringBuilder sb = new StringBuilder();
        for (Perk perk : cache.values()) {
            sb.append("Perk: ").append(perk.getName())
                    .append(", Base Chance: ").append(perk.getBaseChance()).append("\n");

            for (PerkLevel level : perk.getLevels().values()) {
                sb.append("  Level ").append(level.getLevel())
                        .append(" (Chance: ").append(level.getChance())
                        .append(", Display: ").append(level.getDisplay()).append(")\n");

                for (PerkBoost boost : level.getBoosts()) {
                    sb.append("    - Type: ").append(boost.getBoostType())
                            .append(", Target: ").append(boost.getBoostTarget())
                            .append(", Amount: ").append(boost.getAmount()).append("\n");
                }
            }
        }
        return sb.toString();
    }


    public Map<String, Perk> getAllPerks() {
        return cache;
    }
}
