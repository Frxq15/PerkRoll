package me.frxq.perkroll;

import me.frxq.perkroll.command.CommandHandler;
import me.frxq.perkroll.datafactory.DataFactory;
import me.frxq.perkroll.events.CurrencyBoostListener;
import me.frxq.perkroll.file.FileManager;
import me.frxq.perkroll.integration.IntegrationManager;
import me.frxq.perkroll.manager.LocaleManager;
import me.frxq.perkroll.menu.GUIListeners;
import me.frxq.perkroll.perk.PerkCache;
import me.frxq.perkroll.perk.PerkManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class PerkRoll extends JavaPlugin {
    private static PerkRoll instance;

    private IntegrationManager integrationManager;
    private FileManager fileManager;
    private DataFactory dataFactory;
    private PerkCache perkCache;
    private CommandHandler commandHandler;
    private LocaleManager localeManager;
    private PerkManager perkManager;
    private boolean listenersRegistered = false;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        registry();
    }

    @Override
    public void onDisable() {
        if (dataFactory != null) { dataFactory.terminate(); }
        if (integrationManager != null) { integrationManager.disableAll(); }
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

        if (!integrationManager.allRequiredIntegrationsEnabled()) {
            warn("Missing required integrations, Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        fileManager = new FileManager(this);

        dataFactory = new DataFactory(this);
        if (!dataFactory.initialize()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        perkCache = new PerkCache(this);
        perkManager = new PerkManager(this);

        localeManager = new LocaleManager(this);
        localeManager.createLocaleFile();

        commandHandler = new CommandHandler(this);
        commandHandler.load();

        if (!listenersRegistered) {
            registerListeners();
            listenersRegistered = true;
        }
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);
        Bukkit.getPluginManager().registerEvents(new CurrencyBoostListener(this), this);
    }

    public void reload() {
        if (dataFactory != null) { dataFactory.terminate(); }
        if (integrationManager != null) { integrationManager.disableAll(); }
        reloadConfig();
        Bukkit.getScheduler().runTaskLater(this, this::registry, 40L);
    }

    public IntegrationManager getIntegrationManager() { return integrationManager; }
    public FileManager getFileManager() { return fileManager; }
    public DataFactory getDataFactory() { return dataFactory; }
    public PerkCache getPerkCache() { return perkCache; }
    public CommandHandler getCommandHandler() { return commandHandler; }
    public LocaleManager getLocaleManager() { return localeManager; }
    public PerkManager getPerkManager() { return perkManager; }
}
