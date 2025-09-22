package me.frxq.perkroll.command;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.menu.RollMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class testCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 1) {
            PerkRoll.getInstance().getPerkCache().debugAllPerks();
            return true;
        }
        Player player = (Player) commandSender;
        GPlayer gPlayer = PerkRoll.getInstance().getDataFactory().getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
        Bukkit.broadcastMessage(gPlayer.getName() + " has " + gPlayer.getTickets() + " tickets.");
        new RollMenu(PerkRoll.getInstance(), gPlayer).open(player);
        return true;
    }
}
