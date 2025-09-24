package me.frxq.perkroll.menu;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class PerksMenu extends GUITemplate {
    private PerkRoll plugin;
    private GPlayer gPlayer;
    private FileConfiguration file;

    public PerksMenu(PerkRoll plugin, GPlayer gPlayer) {
        super(plugin, plugin.getFileManager().getPerksMenuFile().getInt("ROWS"),
                plugin.getFileManager().getPerksMenuFile().getString("TITLE"));
        this.plugin = plugin;
        this.gPlayer = gPlayer;
        this.file = plugin.getFileManager().getPerksMenuFile();
        register();
    }
    public void register() {
        setMiscItems(file, new HashMap<>(), gPlayer.getPlayer(), false);
    }
}
