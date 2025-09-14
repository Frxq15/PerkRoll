package me.frxq.perkroll.command;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.menu.RollMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class testCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        GPlayer gPlayer = GPlayer.getPlayerData(PerkRoll.getInstance(), player.getUniqueId());
        new RollMenu(PerkRoll.getInstance(), gPlayer).open(player);
        return true;
    }
}
