package me.frxq.perkroll.perk;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.format.ColorFormatter;
import me.frxq.perkroll.format.NumberFormatter;
import me.frxq.perkroll.integration.IntegrationType;
import me.frxq.perkroll.integration.integrations.EdDungeonsIntegration;
import me.frxq.perkroll.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;

public class PerkManager {
    private final PerkRoll plugin;
    private final PerkCache cache;
    private final EdDungeonsIntegration integration;

    public PerkManager(PerkRoll plugin) {
        this.plugin = plugin;
        this.cache = plugin.getPerkCache();
        this.integration = (EdDungeonsIntegration) plugin.getIntegrationManager().getIntegration(IntegrationType.EDDUNGEONS);
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
        playSound(player);
        Bukkit.broadcastMessage(ColorFormatter.format("&b"+player.getName()+" &7rolled the " + boost.getDisplay() + " &7sword perk."));
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
        if(gPlayer.getTickets() < 1) {
            plugin.getLocaleManager().sendMessage(gPlayer.getPlayer(), "NOT_ENOUGH_TICKETS");
            return false;
        }
        gPlayer.setTickets(gPlayer.getTickets() - 1);
        getRandom(gPlayer.getPlayer());
        return true;
    }
    public void checkTicketPurchase(GPlayer gPlayer, BigDecimal cost, int amount) {
        String currency = plugin.getConfig().getString("shop.currency");

        if (cost.compareTo(BigDecimal.ZERO) <= 0) {
            plugin.warn("RollMenu: Invalid cost " + cost + " provided for ticket purchase");
            return;
        }

        BigDecimal balance = integration.getCurrencyAPI().getCurrency(gPlayer.getUUID(), currency);

        if (balance.compareTo(cost) < 0) {
            gPlayer.getPlayer().sendMessage(
                    plugin.getLocaleManager().getMessage("NOT_ENOUGH_FUNDS")
                            .replace("%amount%", String.valueOf(amount))
                            .replace("%cost%", NumberFormatter.formatNumber(cost.longValue()))
                            .replace("%currency%", StringUtils.capitalize(currency))
            );

            return;
        }

        BigDecimal newBalance = balance.subtract(cost);
        integration.getCurrencyAPI().setCurrency(gPlayer.getUUID(), currency, newBalance);
        gPlayer.addTickets(amount);
        gPlayer.getPlayer().sendMessage(
                plugin.getLocaleManager().getMessage("TICKETS_PURCHASED")
                        .replace("%amount%", String.valueOf(amount))
                        .replace("%cost%", NumberFormatter.formatNumber(cost.longValue()))
                        .replace("%currency%", StringUtils.capitalize(currency))
                        .replace("%tickets%", String.valueOf(gPlayer.getTickets()))
        );

    }

}
