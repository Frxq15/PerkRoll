package me.frxq.perkroll.file;

import me.frxq.perkroll.PerkRoll;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileManager {
    private final PerkRoll plugin;
    private final String integrationName;

    private File integrationConfigFile;
    private FileConfiguration integrationConfig;
    private File rollMenuFile;
    private FileConfiguration rollMenuConfig;
    private File perksMenuFile;
    private FileConfiguration perksMenuConfig;
    private File confirmMenuFile;
    private FileConfiguration confirmMenuConfig;
    private File localeFile;
    private FileConfiguration localeConfig;

    public FileManager(PerkRoll plugin, String integrationName) {
        this.plugin = plugin;
        this.integrationName = integrationName;
        createIntegrationConfigFile();
        createRollMenuFile();
        createPerksMenuFile();
        createConfirmMenuFile();
        createLocaleFile();
    }

    private File getIntegrationFolder() {
        return new File(plugin.getDataFolder(), "integrations" + File.separator + integrationName);
    }

    private void saveIntegrationResource(String resourceName, File targetFile) {
        if (targetFile.exists()) return;
        targetFile.getParentFile().mkdirs();
        String resourcePath = "integrations/" + integrationName + "/" + resourceName;
        try (InputStream in = plugin.getResource(resourcePath)) {
            if (in != null) {
                Files.copy(in, targetFile.toPath());
                plugin.log("File: " + integrationName + "/" + resourceName + " was created successfully");
            } else {
                plugin.warn("File: Resource not found: " + resourcePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileConfiguration loadYaml(File file) {
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }

    // Integration config (perks, tillGuaranteed, confirm-rarities)

    public void createIntegrationConfigFile() {
        integrationConfigFile = new File(getIntegrationFolder(), "config.yml");
        saveIntegrationResource("config.yml", integrationConfigFile);
        integrationConfig = loadYaml(integrationConfigFile);
    }

    public FileConfiguration getIntegrationConfig() {
        return integrationConfig;
    }

    public void reloadIntegrationConfig() {
        integrationConfig = YamlConfiguration.loadConfiguration(integrationConfigFile);
    }

    // Roll menu

    public void createRollMenuFile() {
        rollMenuFile = new File(getIntegrationFolder(), "roll-menu.yml");
        saveIntegrationResource("roll-menu.yml", rollMenuFile);
        rollMenuConfig = loadYaml(rollMenuFile);
    }

    public FileConfiguration getRollMenuFile() {
        return rollMenuConfig;
    }

    public void reloadRollMenuFile() {
        rollMenuConfig = YamlConfiguration.loadConfiguration(rollMenuFile);
    }

    public void saveRollMenuFile() {
        try { rollMenuConfig.save(rollMenuFile); } catch (IOException e) { e.printStackTrace(); }
    }

    // Perks menu

    public void createPerksMenuFile() {
        perksMenuFile = new File(plugin.getDataFolder(), "perks-menu.yml");
        if (!perksMenuFile.exists()) {
            perksMenuFile.getParentFile().mkdirs();
            plugin.log("File: perks-menu.yml was created successfully");
            plugin.saveResource("perks-menu.yml", false);
        }
        perksMenuConfig = loadYaml(perksMenuFile);
    }

    public FileConfiguration getPerksMenuFile() {
        return perksMenuConfig;
    }

    public void reloadPerksMenuFile() {
        perksMenuConfig = YamlConfiguration.loadConfiguration(perksMenuFile);
    }

    public void savePerksMenuFile() {
        try { perksMenuConfig.save(perksMenuFile); } catch (IOException e) { e.printStackTrace(); }
    }

    // Confirm menu

    public void createConfirmMenuFile() {
        confirmMenuFile = new File(getIntegrationFolder(), "confirm-menu.yml");
        saveIntegrationResource("confirm-menu.yml", confirmMenuFile);
        confirmMenuConfig = loadYaml(confirmMenuFile);
    }

    public FileConfiguration getConfirmMenuFile() {
        return confirmMenuConfig;
    }

    public void reloadConfirmMenuFile() {
        confirmMenuConfig = YamlConfiguration.loadConfiguration(confirmMenuFile);
    }

    public void saveConfirmMenuFile() {
        try { confirmMenuConfig.save(confirmMenuFile); } catch (IOException e) { e.printStackTrace(); }
    }

    // Locale

    public void createLocaleFile() {
        localeFile = new File(getIntegrationFolder(), "locale.yml");
        saveIntegrationResource("locale.yml", localeFile);
        localeConfig = loadYaml(localeFile);
    }

    public FileConfiguration getLocaleFile() {
        return localeConfig;
    }

    public void reloadLocaleFile() {
        localeConfig = YamlConfiguration.loadConfiguration(localeFile);
    }

    public void saveLocaleFile() {
        try { localeConfig.save(localeFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public String getIntegrationName() {
        return integrationName;
    }
}
