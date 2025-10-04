package me.frxq.perkroll.command.commands.subcommands;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.command.SubCommand;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.format.ColorFormatter;
import me.frxq.perkroll.perk.ActivePerk;
import me.frxq.perkroll.perk.Perk;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SetCommand extends SubCommand {
    private final PerkRoll plugin;

    public SetCommand(PerkRoll plugin) {
        super("set", "perkroll.command.set", "/perkroll set <player> <perk> <level>", null);
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 3) {

            String targetPlayer = args[0];
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetPlayer);
            if(plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(offlinePlayer.getUniqueId()) == null) {
                plugin.getLocaleManager().sendMessage(sender, "PLAYER_NOT_FOUND");
                return;
            }
            GPlayer gPlayer = plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(offlinePlayer.getUniqueId());

            String targetPerk = args[1];
            if(!plugin.getPerkCache().getAllPerks().containsKey(targetPerk.toLowerCase())) {
                plugin.getLocaleManager().sendMessage(sender, "PERK_NOT_FOUND");
                return;
            }

            int level;
            try {
                level = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                plugin.getLocaleManager().sendMessage(sender, "INVALID_INPUT");
                return;
            }
            if(level <= 0) {
                plugin.getLocaleManager().sendMessage(sender, "INVALID_INPUT");
                return;
            }
            Perk perk = plugin.getPerkCache().getPerk(targetPerk.toUpperCase());

            if(level > perk.getMaxLevel()) {
                plugin.getLocaleManager().sendMessage(sender, "PERK_LEVEL_TOO_HIGH");
                return;
            }

            ActivePerk activePerk = new ActivePerk(perk, perk.getLevel(level));
            gPlayer.setActivePerk(activePerk);

            sender.sendMessage(plugin.getLocaleManager().getMessage("PERK_SET")
                    .replace("%player%", gPlayer.getName())
                    .replace("%perk%", ColorFormatter.format(activePerk.getDisplay())));

            if(gPlayer.getPlayer().isOnline()) {
                gPlayer.getPlayer().sendMessage(plugin.getLocaleManager().getMessage("PERK_UPDATED")
                        .replace("%perk%", ColorFormatter.format(activePerk.getDisplay())));
                return;
            }
        }
        plugin.getLocaleManager().sendUsageMessage(sender, getUsage());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return List.of();
    }
}
