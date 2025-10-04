package me.frxq.perkroll.menu;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ConfirmMenu extends GUITemplate {
    private PerkRoll plugin;
    private GPlayer gPlayer;
    private FileConfiguration file;

    public ConfirmMenu(PerkRoll plugin, GPlayer gPlayer) {
        super(plugin, plugin.getFileManager().getConfirmMenuFile().getInt("ROWS"),
                plugin.getFileManager().getConfirmMenuFile().getString("TITLE"));
        this.plugin = plugin;
        this.gPlayer = gPlayer;
        this.file = plugin.getFileManager().getConfirmMenuFile();
        register();
    }
    public void register() {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%tickets%", String.valueOf(gPlayer.getTickets()));
        placeholders.put("%perk%", gPlayer.getActivePerk().getDisplay());
        setMiscItems(file, placeholders, gPlayer.getPlayer(), false);

        file.getStringList("ITEMS.CONFIRM.SLOTS").forEach(s -> {
            int slot = Integer.parseInt(s);
            setItem(slot,
                    createItem(file, "ITEMS.CONFIRM", placeholders, gPlayer.getName(), true),
                    p -> {
                        if(plugin.getPerkManager().checkRollPurchase(gPlayer)) {
                            new RollMenu(plugin, gPlayer).open(p);
                            return;
                        }
                        p.closeInventory();
                    });
        });
        file.getStringList("ITEMS.DECLINE.SLOTS").forEach(s -> {
            int slot = Integer.parseInt(s);
            setItem(slot,
                    createItem(file, "ITEMS.DECLINE", placeholders, gPlayer.getName(), true),
                    p -> {
                            new RollMenu(plugin, gPlayer).open(p);
                    });
        });
    }
}
