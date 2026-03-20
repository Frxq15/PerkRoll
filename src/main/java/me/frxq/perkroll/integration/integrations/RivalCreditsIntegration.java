package me.frxq.perkroll.integration.integrations;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.CurrencyProvider;
import me.frxq.perkroll.integration.Integration;
import me.rivaldev.credits.CreditAPI;
import org.bukkit.Bukkit;

import java.util.UUID;

public class RivalCreditsIntegration extends Integration implements CurrencyProvider {
    private boolean isEnabled;

    public RivalCreditsIntegration(PerkRoll plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "RivalCredits";
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("RivalCredits") != null) {
            isEnabled = true;
            plugin.log("Integrations: Enabled " + getName() + " integration");
            return;
        }
        plugin.warn("Integrations: " + getName() + " not found, skipping");
        isEnabled = false;
    }

    @Override
    public void disable() {
        isEnabled = false;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    // CurrencyProvider

    @Override
    public String getCurrencyId() {
        return "rivalcredits";
    }

    @Override
    public double getCurrencyAmount(UUID uuid, String currency) {
        if (!isEnabled) return 0;
        return CreditAPI.getInstance().getCredits(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public void takeCurrency(UUID uuid, String currency, double amount) {
        if (!isEnabled) return;
        CreditAPI.getInstance().removeCredits(Bukkit.getOfflinePlayer(uuid), amount);
    }
}
