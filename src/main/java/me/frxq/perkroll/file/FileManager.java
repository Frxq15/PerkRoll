package me.frxq.perkroll.file;

import me.frxq.perkroll.PerkRoll;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    private PerkRoll plugin;

    public File RollMenuFile;
    public FileConfiguration RollMenuConfig;
    public File PerksMenuFile;
    public FileConfiguration PerksMenuConfig;
    public File ConfirmMenuFile;
    public FileConfiguration ConfirmMenuConfig;

    public FileManager(PerkRoll plugin) {
        this.plugin = plugin;
        createRollMenuFile();
        createPerksMenuFile();
        createConfirmMenuFile();
    }
    public FileConfiguration getRollMenuFile() {
        return RollMenuConfig;
    }

    public void createRollMenuFile() {
        RollMenuFile = new File(plugin.getDataFolder(), "roll-menu.yml");
        if (!RollMenuFile.exists()) {
            RollMenuFile.getParentFile().mkdirs();
            plugin.log("File: roll-menu.yml was created successfully");
            plugin.saveResource("roll-menu.yml", false);
        }

        RollMenuConfig = new YamlConfiguration();
        try {
            RollMenuConfig.load(RollMenuFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadRollMenuFile() { RollMenuConfig = YamlConfiguration.loadConfiguration(RollMenuFile); }
    public void saveRollMenuFile() {
        try {
            RollMenuConfig.save(RollMenuFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getPerksMenuFile() {
        return PerksMenuConfig;
    }

    public void createPerksMenuFile() {
        PerksMenuFile = new File(plugin.getDataFolder(), "perks-menu.yml");
        if (!PerksMenuFile.exists()) {
            PerksMenuFile.getParentFile().mkdirs();
            plugin.log("File: perks-menu.yml was created successfully");
            plugin.saveResource("perks-menu.yml", false);
        }

        PerksMenuConfig = new YamlConfiguration();
        try {
            PerksMenuConfig.load(PerksMenuFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadPerksMenuFile() { PerksMenuConfig = YamlConfiguration.loadConfiguration(PerksMenuFile); }
    public void savePerksMenuFile() {
        try {
            PerksMenuConfig.save(PerksMenuFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getConfirmMenuFile() {
        return ConfirmMenuConfig;
    }

    public void createConfirmMenuFile() {
        ConfirmMenuFile = new File(plugin.getDataFolder(), "confirm-menu.yml");
        if (!ConfirmMenuFile.exists()) {
            ConfirmMenuFile.getParentFile().mkdirs();
            plugin.log("File: confirm-menu.yml was created successfully");
            plugin.saveResource("confirm-menu.yml", false);
        }

        ConfirmMenuConfig = new YamlConfiguration();
        try {
            ConfirmMenuConfig.load(ConfirmMenuFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public void reloadConfirmMenuFile() { ConfirmMenuConfig = YamlConfiguration.loadConfiguration(ConfirmMenuFile); }
    public void saveConfirmMenuFile() {
        try {
            ConfirmMenuConfig.save(ConfirmMenuFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
