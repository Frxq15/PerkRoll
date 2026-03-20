package me.frxq.perkroll.events;

import es.edwardbelt.eddungeons.iapi.event.EdDungeonsCurrencyAddEvent;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.perk.PerkBoost;
import me.frxq.perkroll.perk.PerkType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;

public class CurrencyBoostListener implements Listener {
    private final PerkRoll plugin;

    public CurrencyBoostListener(PerkRoll plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGain(EdDungeonsCurrencyAddEvent event) {
        GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(event.getUuid());
        if (gPlayer == null || !gPlayer.hasActivePerk()) return;

        BigDecimal amount = event.getAmount();

        for (PerkBoost boost : gPlayer.getActivePerk().getBoosts()) {
            if (boost.getBoostType() == PerkType.CURRENCY
                    && event.getCurrency().equalsIgnoreCase(boost.getBoostTarget())) {
                double multiplier = 1 + (boost.getAmount() / 100.0);
                amount = amount.multiply(BigDecimal.valueOf(multiplier));
            }
        }
        event.setAmount(amount);
    }
}
