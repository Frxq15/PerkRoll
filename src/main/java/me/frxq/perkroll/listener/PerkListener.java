package me.frxq.perkroll.listener;

import com.edwardbelt.edprison.api.models.EconomyModel;
import com.edwardbelt.edprison.events.EdPrisonAddMultiplierCurrency;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.perk.ActivePerk;
import me.frxq.perkroll.perk.PerkBoost;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PerkListener implements Listener {
    private final PerkRoll plugin;
    private final EconomyModel economy;

    public PerkListener(PerkRoll plugin, EconomyModel economyModel) {
        this.plugin = plugin;
        this.economy = economyModel;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCurrencyGainBoost(EdPrisonAddMultiplierCurrency event) {
        GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(event.getUUID());
        if (gPlayer == null || !gPlayer.hasActivePerk()) return;

        ActivePerk activePerk = gPlayer.getActivePerk();
        if (activePerk.getPerk() == null) return;

        if (activePerk.getBoosts().stream().noneMatch(b -> b.getBoostTarget().equalsIgnoreCase(event.getCurrency()))) {
            return;
        }

        double boostPercent = activePerk.getBoosts().stream()
                .filter(b -> b.getBoostTarget().equalsIgnoreCase(event.getCurrency()))
                .findFirst()
                .map(PerkBoost::getAmount)
                .orElse(0.0);

        double total = event.getAmount() * (1 + boostPercent / 100.0);
        economy.addEco(event.getUUID(), event.getCurrency(), total);
    }
}
