package me.frxq.perkroll.integration.integrations;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.Integration;
import me.frxq.perkroll.integration.IntegrationType;
import me.rivaldev.credits.CreditAPI;
import org.bukkit.Bukkit;

import java.util.UUID;

public class RivalCreditsIntegration extends Integration {
    private boolean isEnabled;

    public RivalCreditsIntegration(PerkRoll plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "RivalCredits";
    }

    @Override
    public IntegrationType getType() {
        return IntegrationType.RIVAL_CREDITS;
    }

    @Override
    public void register() {
        enable();
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("RivalCredits") != null) {
            isEnabled = true;
            plugin.log("Integrations: Enabled " + getName() + " integration");
            return;
        }
        plugin.warn("Integrations: Failed to enable " + getName() + " integration, jar could not be found");
        isEnabled = false;
    }

    @Override
    public void disable() {
        isEnabled = false;
    }

    @Override
    public void reload() {

    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public double getCurrencyAmount(UUID uuid, String currency) {
        if(!isEnabled()) return 0;
        return getCreditAPI().getCredits(Bukkit.getOfflinePlayer(uuid));
    }
    @Override
    public void takeCurrency(UUID uuid, String currency, double amount) {
        if(!isEnabled()) return;
        getCreditAPI().removeCredits(Bukkit.getOfflinePlayer(uuid), amount);
    }
    public CreditAPI getCreditAPI() {
        return CreditAPI.getInstance();
    }
}