package me.frxq.perkroll.integration;

import me.frxq.perkroll.PerkRoll;

public enum IntegrationType {
    EDPRISON(PerkRoll.getInstance().getIntegrationManager().getIntegrationByName("EdPrison")),
    RIVAL_CREDITS(PerkRoll.getInstance().getIntegrationManager().getIntegrationByName("RivalCredits")),
    PLACEHOLDERAPI(PerkRoll.getInstance().getIntegrationManager().getIntegrationByName("PlaceholderAPI"));

    private final Integration integration;

    IntegrationType(Integration integration) {
        this.integration = integration;
    }

    public Integration getIntegration() {
        return integration;
    }
}
