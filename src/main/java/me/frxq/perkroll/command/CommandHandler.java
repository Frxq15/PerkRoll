package me.frxq.perkroll.command;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.command.commands.PerkRollCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class CommandHandler {
    private final PerkRoll plugin;

    public CommandHandler(PerkRoll plugin) {
        this.plugin = plugin;
    }

    public void load() {
        registerCommands();
    }

    private void registerCommands() {
        registerCommand("perkroll", new PerkRollCommand(plugin));
        plugin.getCommand("test").setExecutor(new testCommand());
    }

    public void registerCommand(String name, CommandExecutor commandExecutor) {
        PluginCommand command = plugin.getCommand(name);

        if (command == null) {
            plugin.warn("Command Registry: Command " + name + " is not registered in the plugin.yml! Skipping command...");
            return;
        }

        command.setExecutor(commandExecutor);

        if (commandExecutor instanceof TabCompleter)
            command.setTabCompleter((TabCompleter) commandExecutor);
    }
    public void registerSubCommand(String parentCommand, SubCommand subCommand) {
        CommandExecutor executor = plugin.getCommand(parentCommand).getExecutor();

        if (executor instanceof ParentCommand) {
            ((ParentCommand) executor).register(subCommand);
        } else {
            plugin.error("Command Handler: Failed to register subcommand: " + subCommand.getCommand() + " to " + parentCommand);
        }
    }
    public void unregisterGangSubCommand(String subCommandLabel) {
        CommandExecutor executor = plugin.getCommand("gang").getExecutor();
        if (executor instanceof ParentCommand) {
            ParentCommand parent = (ParentCommand) executor;
            parent.unregister(subCommandLabel);
        } else {
            plugin.error("Failed to unregister subcommand: " + subCommandLabel + " from gang command");
        }
    }

}
