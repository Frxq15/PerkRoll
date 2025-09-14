package me.frxq.perkroll.integration;

import me.frxq.perkroll.PerkRoll;

public enum IntegrationType {
    EDDUNGEONS(PerkRoll.getInstance().getIntegrationManager().getIntegrationByName("EdDungeons")),
    PLACEHOLDERAPI(PerkRoll.getInstance().getIntegrationManager().getIntegrationByName("PlaceholderAPI"));

    private final Integration integration;

    IntegrationType(Integration integration) {
        this.integration = integration;
    }

    public Integration getIntegration() {
        return integration;
    }
}
