package me.frxq.perkroll.command.commands.subcommands;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.command.SubCommand;
import me.frxq.perkroll.datafactory.player.GPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GiveCommand extends SubCommand {
    private final PerkRoll plugin;

    public GiveCommand(PerkRoll plugin) {
        super("give", "perkroll.command.give", "/perkroll give <player> <amount>", List.of("add"));
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 2) {
            String targetPlayer = args[0];
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetPlayer);
            if(plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(offlinePlayer.getUniqueId()) == null) {
                plugin.getLocaleManager().sendMessage(sender, "PLAYER_NOT_FOUND");
                return;
            }
            GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(offlinePlayer.getUniqueId());
            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                plugin.getLocaleManager().sendMessage(sender, "INVALID_INPUT");
                return;
            }
            if(amount <= 0) {
                plugin.getLocaleManager().sendMessage(sender, "INVALID_INPUT");
                return;
            }
            gPlayer.addTickets(amount);
            sender.sendMessage(plugin.getLocaleManager().getMessage("TICKETS_ADDED").replace("%amount%", String.valueOf(amount)).replace("%player%", gPlayer.getName()));
        }
        plugin.getLocaleManager().sendUsageMessage(sender, getUsage());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) { return getVisiblePlayers(sender); }
        return List.of();
    }
}
