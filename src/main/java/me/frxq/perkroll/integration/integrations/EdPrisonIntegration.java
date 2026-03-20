package me.frxq.perkroll.integration.integrations;

import com.edwardbelt.edprison.EdPrison;
import com.edwardbelt.edprison.api.models.EconomyModel;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.CurrencyProvider;
import me.frxq.perkroll.integration.Integration;
import me.frxq.perkroll.listener.PerkListener;
import org.bukkit.Bukkit;

import java.util.UUID;

public class EdPrisonIntegration extends Integration implements CurrencyProvider {
    private boolean isEnabled;

    public EdPrisonIntegration(PerkRoll plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "EdPrison";
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("EdPrison") != null) {
            isEnabled = true;
            Bukkit.getPluginManager().registerEvents(new PerkListener(plugin, getEconomyModel()), plugin);
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
        return "edprison";
    }

    @Override
    public double getCurrencyAmount(UUID uuid, String currency) {
        if (!isEnabled) return 0;
        return EdPrison.getInstance().getApi().getEconomyApi().getEco(uuid, currency);
    }

    @Override
    public void takeCurrency(UUID uuid, String currency, double amount) {
        if (!isEnabled) return;
        EdPrison.getInstance().getApi().getEconomyApi().removeEco(uuid, currency, amount);
    }

    public EconomyModel getEconomyModel() {
        return EdPrison.getInstance().getApi().getEconomyApi();
    }
}
