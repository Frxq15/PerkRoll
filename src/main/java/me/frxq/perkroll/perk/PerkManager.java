package me.frxq.perkroll.perk;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.format.ColorFormatter;
import me.frxq.perkroll.format.NumberFormatter;
import me.frxq.perkroll.integration.IntegrationType;
import me.frxq.perkroll.integration.integrations.EdPrisonIntegration;
import me.frxq.perkroll.integration.integrations.RivalCreditsIntegration;
import me.frxq.perkroll.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class PerkManager {
    private final PerkRoll plugin;
    private final PerkCache cache;
    private final EdPrisonIntegration edp;
    private final RivalCreditsIntegration rival;

    public PerkManager(PerkRoll plugin) {
        this.plugin = plugin;
        this.cache = plugin.getPerkCache();
        this.edp = (EdPrisonIntegration) plugin.getIntegrationManager().getIntegration(IntegrationType.EDPRISON);
        this.rival = (RivalCreditsIntegration) plugin.getIntegrationManager().getIntegration(IntegrationType.RIVAL_CREDITS);
    }
    public ActivePerk getRandomActivePerk() {
        double totalPerkChance = 0;
        for (Perk perk : cache.getAllPerks().values()) {
            totalPerkChance += perk.getBaseChance();
        }

        if (totalPerkChance <= 0) {
            return null;
        }

        // Roll perk
        double perkRoll = Math.random() * totalPerkChance;
        Perk chosenPerk = null;
        double cumulativePerk = 0;

        for (Perk perk : cache.getAllPerks().values()) {
            cumulativePerk += perk.getBaseChance();
            if (perkRoll <= cumulativePerk) {
                chosenPerk = perk;
                break;
            }
        }

        if (chosenPerk == null) {
            return null;
        }

        // Roll level inside chosen perk
        double totalLevelChance = 0;
        for (PerkLevel level : chosenPerk.getLevels().values()) {
            totalLevelChance += level.getChance();
        }

        if (totalLevelChance <= 0) {
            return null;
        }

        double levelRoll = Math.random() * totalLevelChance;
        double cumulativeLevel = 0;
        PerkLevel chosenLevel = null;

        for (PerkLevel level : chosenPerk.getLevels().values()) {
            cumulativeLevel += level.getChance();
            if (levelRoll <= cumulativeLevel) {
                chosenLevel = level;
                break;
            }
        }

        if (chosenLevel == null) {
            return null;
        }

        return new ActivePerk(chosenPerk, chosenLevel);
    }
    public void getRandom(Player player) {
        ActivePerk active;
        GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(player.getUniqueId());

        if(gPlayer.getTillGuaranteed() > plugin.getConfig().getInt("tillGuaranteed.amount")) {
            active = getPityLuckRandom(gPlayer);
            player.sendMessage(ColorFormatter.format(plugin.getLocaleManager().getMessage("PERK_ROLLED_PITY").replace("%perk%", active.getDisplay())));
        } else {
            active = getRandomActivePerk();
            player.sendMessage(ColorFormatter.format(plugin.getLocaleManager().getMessage("PERK_ROLLED").replace("%perk%", active.getDisplay())));
        }
        playSound(player);
        gPlayer.setActivePerk(active);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "edp updatepickaxe " + player.getName());
    }

    public void playSound(Player player) {
        if(plugin.getConfig().getBoolean("sound.enabled")) {
            String soundType = plugin.getConfig().getString("sound.sound");
            if (soundType == null || soundType.equalsIgnoreCase("NONE")) {
                return;
            }
            try {
                Sound sound = Sound.valueOf(soundType);
                player.getPlayer().playSound(player.getPlayer().getLocation(), sound, 1.0f, 1.0f);
            } catch (IllegalArgumentException e) {
                plugin.warn("PerkRoll: Invalid sound type provided: " + soundType);
            }
        }
    }
    public boolean checkRollPurchase(GPlayer gPlayer) {
        if (gPlayer.getTickets() < 1) {
            plugin.getLocaleManager().sendMessage(gPlayer.getPlayer(), "NOT_ENOUGH_TICKETS");
            return false;
        }

        gPlayer.setTickets(gPlayer.getTickets() - 1);
        gPlayer.addTicketsUsed(1);

        gPlayer.addTillGuaranteed(1);

        getRandom(gPlayer.getPlayer());
        return true;
    }

    public void checkTicketPurchase(GPlayer gPlayer, String currency, double cost, int amount) {

        if (cost  <= 0) {
            plugin.warn("RollMenu: Invalid cost " + cost + " provided for ticket purchase");
            return;
        }

        double balance = currency.equals("rivalcredits") ?
                rival.getCurrencyAmount(gPlayer.getUUID(), currency) :
                edp.getCurrencyAmount(gPlayer.getUUID(), currency);

        if (balance  < 0) {
            gPlayer.getPlayer().sendMessage(
                    plugin.getLocaleManager().getMessage("NOT_ENOUGH_FUNDS")
                            .replace("%amount%", String.valueOf(amount))
                            .replace("%cost%", NumberFormatter.formatNumber((long)cost))
                            .replace("%currency%", StringUtils.capitalize(currency))
            );

            return;
        }

        if(currency.equals("rivalcredits")) {
            rival.takeCurrency(gPlayer.getUUID(), currency, cost);
        } else {
            edp.takeCurrency(gPlayer.getUUID(), currency, cost);
        }

        gPlayer.addTickets(amount);
        gPlayer.getPlayer().sendMessage(
                plugin.getLocaleManager().getMessage("TICKETS_PURCHASED")
                        .replace("%amount%", String.valueOf(amount))
                        .replace("%cost%", NumberFormatter.formatNumber((long)cost))
                        .replace("%currency%", StringUtils.capitalize(currency))
                        .replace("%tickets%", String.valueOf(gPlayer.getTickets()))
        );

    }
    public ActivePerk getPityLuckRandom(GPlayer gPlayer) {
        ConfigurationSection pityTypes = plugin.getConfig().getConfigurationSection("tillGuaranteed.types");

        if (pityTypes == null || pityTypes.getKeys(false).isEmpty()) {
            return null;
        }
        List<String> availableTypes = new ArrayList<>(pityTypes.getKeys(false));
        String chosenType = availableTypes.get(new Random().nextInt(availableTypes.size()));

        Perk chosenPerk = cache.getPerk(chosenType);
        if (chosenPerk == null) {
            return null;
        }
        int min = pityTypes.getInt(chosenType + ".min");
        int max = pityTypes.getInt(chosenType + ".max");

        int rolledLevel = min + new Random().nextInt((max - min) + 1);

        PerkLevel chosenLevel = chosenPerk.getLevels().get(rolledLevel);

        if (chosenLevel == null) {
            chosenLevel = chosenPerk.getLevels().values().stream()
                    .max(Comparator.comparingInt(PerkLevel::getLevel))
                    .orElse(null);
        }
        gPlayer.setTillGuaranteed(0);

        return (chosenLevel != null) ? new ActivePerk(chosenPerk, chosenLevel) : null;
    }

}
