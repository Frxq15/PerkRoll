package me.frxq.perkroll.util;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.integration.integrations.PlaceholderAPIIntegration;
import org.bukkit.OfflinePlayer;

public class StringUtils {

    public static String applyPlaceholders(OfflinePlayer player, String text) {
        PlaceholderAPIIntegration placeholderAPI = PerkRoll.getInstance().getIntegrationManager()
                .getIntegration(PlaceholderAPIIntegration.class);
        if (placeholderAPI != null && placeholderAPI.isEnabled()) {
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
