package me.frxq.perkroll;

import me.frxq.perkroll.command.testCommand;
import me.frxq.perkroll.datafactory.DataFactory;
import me.frxq.perkroll.file.FileManager;
import me.frxq.perkroll.integration.IntegrationManager;
import me.frxq.perkroll.menu.GUIListeners;
import me.frxq.perkroll.perk.PerkCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class PerkRoll extends JavaPlugin {
    public static PerkRoll instance;

    private IntegrationManager integrationManager;
    private FileManager fileManager;
    private DataFactory dataFactory;
    private PerkCache perkCache;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        registry();
    }

    @Override
    public void onDisable() {
    }
    public static PerkRoll getInstance() { return instance; }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[PerkRoll] " + message);
    }
    public void warn(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[PerkRoll] " + message);
    }
    public void error(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[PerkRoll] " + message);
    }

    public void registry() {
        log("Registering PerkRoll v" + getDescription().getVersion());
        integrationManager = new IntegrationManager(this);
        integrationManager.registerIntegrations();

        if(!integrationManager.AllIntegrationsEnabled()) {
            warn("Missing required integrations, Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        fileManager = new FileManager(this);

        dataFactory = new DataFactory(this);
        if(!dataFactory.initialize()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        perkCache = new PerkCache(this);

        getCommand("test").setExecutor(new testCommand());
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);

    }
    public IntegrationManager getIntegrationManager() { return integrationManager; }
    public FileManager getFileManager() { return fileManager; }
    public DataFactory getDataFactory() { return dataFactory; }
    public PerkCache getPerkCache() { return perkCache; }
}
