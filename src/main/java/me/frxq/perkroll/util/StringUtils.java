package me.frxq.perkroll.util;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.IntegrationType;
import me.frxq.perkroll.integration.integrations.PlaceholderAPIIntegration;
import org.bukkit.OfflinePlayer;

public class StringUtils {
    static PerkRoll plugin = PerkRoll.getInstance();
    static PlaceholderAPIIntegration placeholderAPI = (PlaceholderAPIIntegration) plugin.getIntegrationManager().getIntegration(IntegrationType.PLACEHOLDERAPI);

    public static String applyPlaceholders(OfflinePlayer player, String text) {
        if(placeholderAPI.isEnabled()) {
            return placeholderAPI.applyPlaceholders(player, text);
        }
        return text;
    }
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
