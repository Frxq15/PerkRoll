package me.frxq.perkroll.command.commands.subcommands;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends SubCommand {
    private final PerkRoll plugin;

    public ReloadCommand(PerkRoll plugin) {
        super("reload", "perkroll.command.reload", "/perkroll reload", null);
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reload();
        plugin.getLocaleManager().sendMessage(sender, "PLUGIN_RELOADED");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
