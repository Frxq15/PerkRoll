package me.frxq.perkroll.integration;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.integrations.EdPrisonIntegration;
import me.frxq.perkroll.integration.integrations.PlaceholderAPIIntegration;
import me.frxq.perkroll.integration.integrations.RivalCreditsIntegration;

import java.util.ArrayList;
import java.util.List;

public class IntegrationManager {
    private final PerkRoll plugin;
    private final List<Integration> integrations = new ArrayList();

    public IntegrationManager(PerkRoll plugin) {
        this.plugin = plugin;
    }

    public void registerIntegrations() {
        registerIntegration(new EdPrisonIntegration(plugin));
        registerIntegration(new RivalCreditsIntegration(plugin));
        registerIntegration(new PlaceholderAPIIntegration(plugin));
    }

    public void registerIntegration(Integration integration) {
        integrations.add(integration);
        integration.register();

        if(integration.getType() == IntegrationType.PLACEHOLDERAPI) {
            integration.enable();
        }
    }

    public boolean AllIntegrationsEnabled() {
        for(Integration integration : integrations) {
            if(integration.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public List<Integration> getIntegrations() {
        return integrations;
    }

    public Integration getIntegrationByName(String name) {
        for(Integration integration : integrations) {
            if(integration.getName().equalsIgnoreCase(name)) {
                return integration;
            }
        }
        return null;
    }
    public Integration getIntegration(IntegrationType type) {
        for (Integration integration : integrations) {
            if(integration.getType().equals(type)) {
                return integration;
            }
        }
        return null;
    }
}
