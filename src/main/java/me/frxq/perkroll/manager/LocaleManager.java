package me.frxq.perkroll.manager;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.format.ColorFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class LocaleManager {
    private final PerkRoll plugin;

    public LocaleManager(PerkRoll plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(CommandSender sender, String message) {
        if (getLocaleFile().getString(message) == null) {
            plugin.error("Locale: Message not found: " + message);
            return;
        }
        sender.sendMessage(ColorFormatter.format(getLocaleFile().getString(message)));
    }
    public void sendPermissionMessage(CommandSender sender) {
        sender.sendMessage(ColorFormatter.format(getLocaleFile().getString("NO_PERMISSION")));
    }
    public void sendRawMessage(CommandSender sender, String message) {
        sender.sendMessage(ColorFormatter.format(message));
    }
    public void sendUsageMessage(CommandSender sender, String usage) {
        sender.sendMessage(ColorFormatter.format("&cUsage: "+usage));
    }
    public void broadcastMessage(String message) {
        Bukkit.broadcastMessage(ColorFormatter.format(message));
    }
    public String getMessage(String message) {
        if (getLocaleFile().getString(message) == null) {
            plugin.error("Locale: Message not found: " + message);
            return ColorFormatter.format("&cMessage not found");
        }
        return ColorFormatter.format(getLocaleFile().getString(message));
    }

    public FileConfiguration getLocaleFile() {
        return plugin.getFileManager().getLocaleFile();
    }
}
