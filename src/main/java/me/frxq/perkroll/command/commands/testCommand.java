package me.frxq.perkroll.command.commands;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.integration.integrations.EdDungeonsIntegration;
import me.frxq.perkroll.menu.RollMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class testCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        PerkRoll plugin = PerkRoll.getInstance();
        Player p = (Player) commandSender;
        EdDungeonsIntegration ed = (EdDungeonsIntegration) plugin.getIntegrationManager().getIntegration(me.frxq.perkroll.integration.IntegrationType.EDDUNGEONS);
        if(strings.length == 1) {
            ed.getBoosterAPI().getActiveBoosters(p.getUniqueId()).forEach(Bukkit::broadcastMessage);
            return true;
        }
        if(strings.length == 2) {
            Bukkit.broadcastMessage(PerkRoll.getInstance().getPerkCache().debugAllPerks());
            return true;
        }
        Player player = (Player) commandSender;
        GPlayer gPlayer = PerkRoll.getInstance().getDataFactory().getGPlayerDataFactory().getGPlayerData(player.getUniqueId());
        new RollMenu(PerkRoll.getInstance(), gPlayer).open(player);
        return true;
    }
}
