package me.frxq.perkroll.command;

import me.frxq.perkroll.PerkRoll;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.*;

public abstract class ParentCommand implements CommandExecutor, TabCompleter {
    protected final PerkRoll plugin;
    protected final String name;
    protected final String permission;

    private final Set<SubCommand> subCommands = new HashSet<>();

    protected String subLabel;
    protected String[] subArgs;

    public ParentCommand(PerkRoll plugin, String name, String permission) {
        this.plugin = plugin;
        this.name = name;
        this.permission = permission;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(permission)) return null;

        List<String> results = null;
        if (args.length == 1) {
            results = new ArrayList<>();
            for (SubCommand subCommand : getSubCommands()) {
                if (sender.hasPermission(subCommand.getPermission())) {
                    results.add(subCommand.getCommand());
                    List<String> aliases = subCommand.getAliases() == null ? List.of() : subCommand.getAliases();
                    results.addAll(aliases);
                }
            }
        }

        if (args.length >= 2) {
            results = new ArrayList<>();
            subLabel = args[0];
            subArgs = Arrays.copyOfRange(args, 1, args.length);

            SubCommand subCommand = getExecutor(subLabel);

            if (subCommand != null && sender.hasPermission(subCommand.getPermission()))
                results.addAll(subCommand.onTabComplete(sender, command, subLabel, subArgs));
        }

        if (results != null && !results.isEmpty()) {
            results = StringUtil.copyPartialMatches(args[args.length - 1], results, new ArrayList<>(results.size()));
            results.sort(String.CASE_INSENSITIVE_ORDER);
        }

        return results;
    }

    public void register(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public void unregister(String label) {
        subCommands.removeIf(subCommand ->
                subCommand.getCommand().equalsIgnoreCase(label) ||
                        (subCommand.getAliases() != null && subCommand.getAliases().contains(label))
        );
    }

    public boolean exists(String label) {
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getCommand().equalsIgnoreCase(label))
                return true;
            else if (subCommand.getAliases() != null)
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(label))
                        return true;
        }

        return false;
    }

    public SubCommand getExecutor(String label) {
        for (SubCommand subCommand : subCommands){
            if (subCommand.getCommand().equalsIgnoreCase(label)) {
                return subCommand;
            } else if (subCommand.getAliases() != null) {
                for (String alias : subCommand.getAliases()) {
                    if (alias.equalsIgnoreCase(label)) {
                        return subCommand;
                    }
                }
            }
        }
        return null;
    }

    public Set<SubCommand> getSubCommands() {
        return subCommands;
    }

}
