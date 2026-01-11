package me.frxq.perkroll.integration.integrations;

import com.edwardbelt.edprison.EdPrison;
import com.edwardbelt.edprison.api.models.EconomyModel;
import com.edwardbelt.edprison.api.models.UtilsModel;
import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.Integration;
import me.frxq.perkroll.integration.IntegrationType;
import me.frxq.perkroll.listener.PerkListener;
import org.bukkit.Bukkit;

import java.util.UUID;

public class EdPrisonIntegration extends Integration {
    private boolean isEnabled;

    public EdPrisonIntegration(PerkRoll plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "EdPrison";
    }

    @Override
    public IntegrationType getType() {
        return IntegrationType.EDPRISON;
    }

    @Override
    public void register() {
        enable();
    }

    @Override
    public void enable() {
        if (Bukkit.getPluginManager().getPlugin("EdPrison") != null) {
            isEnabled = true;
            Bukkit.getPluginManager().registerEvents(new PerkListener(plugin, getEconomyModel()), plugin);
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
        return EdPrison.getInstance().getApi().getEconomyApi().getEco(uuid, currency);
    }
    @Override
    public void takeCurrency(UUID uuid, String currency, double amount) {
        if(!isEnabled()) return;
        EdPrison.getInstance().getApi().getEconomyApi().removeEco(uuid, currency, amount);
    }
    public EconomyModel getEconomyModel() {
        return EdPrison.getInstance().getApi().getEconomyApi();
    }
}

