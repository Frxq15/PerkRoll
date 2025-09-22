package me.frxq.perkroll.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SubCommand {
    private final String command;
    private final String permission;
    private final String usage;
    private final List<String> aliases;

    public SubCommand(String command, String permission, String usage, List<String> aliases) {
        this.command = command;
        this.permission = permission;
        this.usage = usage;
        this.aliases = aliases;
    }
    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return usage;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public abstract void onCommand(CommandSender sender, Command command, String label, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);

    public List<String> getVisiblePlayers(CommandSender sender) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> !(sender instanceof Player) || ((Player) sender).canSee(player))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

}
