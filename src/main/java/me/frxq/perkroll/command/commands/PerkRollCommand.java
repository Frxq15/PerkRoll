package me.frxq.perkroll.command.commands;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.command.ParentCommand;
import me.frxq.perkroll.command.SubCommand;
import me.frxq.perkroll.command.commands.subcommands.GiveCommand;
import me.frxq.perkroll.command.commands.subcommands.ReloadCommand;
import me.frxq.perkroll.command.commands.subcommands.SetCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class PerkRollCommand extends ParentCommand {

    public PerkRollCommand(PerkRoll plugin) {
        super(plugin, "perkroll", "perkroll.command.main");
        register(new GiveCommand(plugin));
        register(new ReloadCommand(plugin));
        register(new SetCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(permission)) {
            plugin.getLocaleManager().sendPermissionMessage(sender);
            return true;
        }
        if (args.length == 0) {
            plugin.getLocaleManager().sendMessage(sender, "UNKNOWN_SUBCOMMAND");
            return true;
        }
        subLabel = args[0];
        subArgs = Arrays.copyOfRange(args, 1, args.length);

        if (!exists(subLabel)) {
            plugin.getLocaleManager().sendMessage(sender, "UNKNOWN_SUBCOMMAND");
            return true;
        }
        SubCommand subCommand = getExecutor(subLabel);

        if (!sender.hasPermission(subCommand.getPermission()) && (!sender.hasPermission("perkroll.command.all"))) {
            plugin.getLocaleManager().sendPermissionMessage(sender);
            return true;
        }
        subCommand.onCommand(sender, command, subLabel, subArgs);
        return true;
    }
}
