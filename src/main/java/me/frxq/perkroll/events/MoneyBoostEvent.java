package me.frxq.perkroll.events;

import es.edwardbelt.eddungeons.iapi.event.EdDungeonsCurrencyAddEvent;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.IntegrationType;
import me.frxq.perkroll.integration.integrations.EdDungeonsIntegration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;

public class MoneyBoostEvent implements Listener {
    private final PerkRoll plugin;
    private final EdDungeonsIntegration integration;

    public MoneyBoostEvent(PerkRoll plugin) {
        this.plugin = plugin;
        this.integration = (EdDungeonsIntegration) plugin.getIntegrationManager().getIntegration(IntegrationType.EDDUNGEONS);
    }
    @EventHandler
    public void onGain(EdDungeonsCurrencyAddEvent event) {
        if(event.getCurrency().equalsIgnoreCase("money")) {
            event.setAmount(event.getAmount().multiply(BigDecimal.TEN));
            Bukkit.broadcastMessage("new money multi 10");
            Bukkit.broadcastMessage("new money amount " + event.getAmount());
        }
    }
}
