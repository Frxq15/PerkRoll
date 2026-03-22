package me.frxq.perkroll.integration;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.integrations.EdDungeonsIntegration;
import me.frxq.perkroll.integration.integrations.EdPrisonIntegration;
import me.frxq.perkroll.integration.integrations.PlaceholderAPIIntegration;
import me.frxq.perkroll.integration.integrations.RivalCreditsIntegration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntegrationManager {
    private final PerkRoll plugin;
    private final List<Integration> integrations = new ArrayList<>();
    private final Map<String, CurrencyProvider> currencyProviders = new HashMap<>();
    private BoostProvider boostProvider;
    private SwordProvider swordProvider;
    private String activeIntegrationName;

    public IntegrationManager(PerkRoll plugin) {
        this.plugin = plugin;
    }

    public void registerIntegrations() {
        registerIntegration(new EdDungeonsIntegration(plugin));
        registerIntegration(new EdPrisonIntegration(plugin));
        registerIntegration(new RivalCreditsIntegration(plugin));
        registerIntegration(new PlaceholderAPIIntegration(plugin));
    }

    public void registerIntegration(Integration integration) {
        integration.enable();
        integrations.add(integration);

        if (integration.isEnabled() && integration instanceof CurrencyProvider provider) {
            currencyProviders.put(provider.getCurrencyId().toLowerCase(), provider);
        }
        if (integration.isEnabled() && integration instanceof BoostProvider provider) {
            boostProvider = provider;
        }
        if (integration.isEnabled() && integration instanceof SwordProvider provider) {
            swordProvider = provider;
        }
        if (integration.isEnabled() && activeIntegrationName == null
                && (integration instanceof EdDungeonsIntegration || integration instanceof EdPrisonIntegration)) {
            activeIntegrationName = integration.getName();
        }
    }

    public boolean allRequiredIntegrationsEnabled() {
        if (activeIntegrationName == null) {
            plugin.error("Integrations: No primary integration (EdDungeons or EdPrison) is enabled.");
            return false;
        }
        for (Integration integration : integrations) {
            if (integration.isRequired() && !integration.isEnabled()) {
                plugin.error("Integrations: Required integration '" + integration.getName() + "' is not enabled.");
                return false;
            }
        }
        return true;
    }

    public void disableAll() {
        for (Integration integration : integrations) {
            if (integration.isEnabled()) {
                integration.disable();
            }
        }
        integrations.clear();
        currencyProviders.clear();
        boostProvider = null;
        swordProvider = null;
        activeIntegrationName = null;
    }

    public String getActiveIntegrationName() {
        return activeIntegrationName;
    }

    public CurrencyProvider getCurrencyProvider(String currencyId) {
        if (currencyId == null) return null;
        CurrencyProvider provider = currencyProviders.get(currencyId.toLowerCase());
        if (provider != null) return provider;
        return currencyProviders.values().stream().findFirst().orElse(null);
    }

    public BoostProvider getBoostProvider() {
        return boostProvider;
    }

    public SwordProvider getSwordProvider() {
        return swordProvider;
    }

    @SuppressWarnings("unchecked")
    public <T extends Integration> T getIntegration(Class<T> type) {
        for (Integration integration : integrations) {
            if (type.isInstance(integration)) {
                return (T) integration;
            }
        }
        return null;
    }

    public List<Integration> getIntegrations() {
        return integrations;
    }
}
