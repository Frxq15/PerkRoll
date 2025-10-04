package me.frxq.perkroll.command.commands;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PayPerksCommand implements CommandExecutor {
    private final PerkRoll plugin = PerkRoll.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            PerkRoll.getInstance().log("This command cannot be executed from console.");
            return true;
        }
        if(!commandSender.hasPermission("perkroll.command.payperks")) {
            plugin.getLocaleManager().sendPermissionMessage(commandSender);
            return true;
        }
        if(args.length == 2) {
            Player player = (Player) commandSender;
            String target = args[0];

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(target);
            if(plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(offlinePlayer.getUniqueId()) == null) {
                plugin.getLocaleManager().sendMessage(player, "PLAYER_NOT_FOUND");
                return true;
            }
            if(offlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                plugin.getLocaleManager().sendMessage(commandSender, "CANNOT_PAY_SELF");
                return true;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                plugin.getLocaleManager().sendMessage(commandSender, "INVALID_INPUT");
                return true;
            }
            if(amount <= 0) {
                plugin.getLocaleManager().sendMessage(commandSender, "INVALID_INPUT");
                return true;
            }
            GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
            GPlayer tPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(offlinePlayer.getUniqueId());

            if(gPlayer.getTickets() < amount) {
                plugin.getLocaleManager().sendMessage(commandSender, "NOT_ENOUGH_FUNDS_PAY");
                return true;
            }
            gPlayer.setTickets(gPlayer.getTickets() - amount);
            tPlayer.addTickets(amount);
            player.sendMessage(plugin.getLocaleManager().getMessage("TICKETS_PAID")
                    .replace("%player%", tPlayer.getName())
                    .replace("%amount%", String.valueOf(amount))
                    .replace("%tickets%", String.valueOf(gPlayer.getTickets())));

            if(tPlayer.getPlayer() != null && tPlayer.getPlayer().isOnline()) {
                tPlayer.getPlayer().sendMessage(plugin.getLocaleManager().getMessage("TICKETS_RECEIVED")
                        .replace("%player%", gPlayer.getName())
                        .replace("%amount%", String.valueOf(amount))
                        .replace("%tickets%", String.valueOf(tPlayer.getTickets())));
            }
            return true;
        }
        plugin.getLocaleManager().sendUsageMessage(commandSender, "/payperks <player> <amount>");
        return true;
    }
}
