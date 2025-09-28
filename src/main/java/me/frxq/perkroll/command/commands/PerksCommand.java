package me.frxq.perkroll.command.commands;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.menu.RollMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PerksCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)) {
            PerkRoll.getInstance().log("This command cannot be executed from console.");
            return true;
        }
        Player player = (Player) commandSender;
        if(!player.hasPermission("perkroll.command.perks")) {
            PerkRoll.getInstance().getLocaleManager().sendPermissionMessage(player);
            return true;
        }
        GPlayer gPlayer = PerkRoll.getInstance().getDataFactory().getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
        new RollMenu(PerkRoll.getInstance(), gPlayer).open(player);
        return true;
    }
}
