package me.frxq.perkroll.manager;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.format.ColorFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocaleManager {
    private PerkRoll plugin;
    public File localeFile;
    public FileConfiguration localeConfig;

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

    public void createLocaleFile() {
        localeFile = new File(plugin.getDataFolder(), "locale.yml");
        if (!localeFile.exists()) {
            localeFile.getParentFile().mkdirs();
            plugin.log("File: locale.yml was created successfully");
            plugin.saveResource("locale.yml", false);
        }

        localeConfig = new YamlConfiguration();
        try {
            localeConfig.load(localeFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadLocaleFile() { localeConfig = YamlConfiguration.loadConfiguration(localeFile); }
    public void saveLocaleFile() {
        try {
            localeConfig.save(localeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getLocaleFile() { return localeConfig; }
}
