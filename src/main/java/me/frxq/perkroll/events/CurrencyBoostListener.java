package me.frxq.perkroll.events;

import es.edwardbelt.eddungeons.iapi.event.EdDungeonsCurrencyAddEvent;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.integration.IntegrationType;
import me.frxq.perkroll.integration.integrations.EdPrisonIntegration;
import me.frxq.perkroll.perk.PerkBoost;
import me.frxq.perkroll.perk.PerkType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;

public class CurrencyBoostListener implements Listener {
    private final PerkRoll plugin;
    private final EdPrisonIntegration integration;

    public CurrencyBoostListener(PerkRoll plugin) {
        this.plugin = plugin;
        this.integration = (EdPrisonIntegration) plugin.getIntegrationManager().getIntegration(IntegrationType.EDDUNGEONS);
    }
    @EventHandler
    public void onGain(EdDungeonsCurrencyAddEvent event) {
        GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(event.getUuid());
        if (gPlayer == null || !gPlayer.hasActivePerk()) return;

        BigDecimal amount = event.getAmount();

        for (PerkBoost boost : gPlayer.getActivePerk().getBoosts()) {
            if (boost.getBoostType().equals(PerkType.CURRENCY)) {
                if (event.getCurrency().equalsIgnoreCase(boost.getBoostTarget())) {
                    double multiplier = 1 + (boost.getAmount() / 100.0);
                    amount = amount.multiply(BigDecimal.valueOf(multiplier));
                   // DEBUG: Bukkit.broadcastMessage("Applied currency boost: +" + boost.getAmount() + "% "
                         //   + boost.getBoostTarget() + " → new amount = " + amount);
                }
            }
        }
        event.setAmount(amount);
    }

}
