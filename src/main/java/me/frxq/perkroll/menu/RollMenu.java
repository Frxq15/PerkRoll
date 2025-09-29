package me.frxq.perkroll.menu;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.integration.IntegrationType;
import me.frxq.perkroll.integration.integrations.EdDungeonsIntegration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class RollMenu extends GUITemplate {
    private final PerkRoll plugin;
    private final GPlayer gPlayer;
    private final FileConfiguration file;
    private final EdDungeonsIntegration integration;

    public RollMenu(PerkRoll plugin, GPlayer gPlayer) {
        super(plugin,
                plugin.getFileManager().getRollMenuFile().getInt("ROWS"),
                plugin.getFileManager().getRollMenuFile().getString("TITLE"));
        this.plugin = plugin;
        this.integration = (EdDungeonsIntegration) plugin.getIntegrationManager()
                .getIntegration(IntegrationType.EDDUNGEONS);
        this.gPlayer = gPlayer;
        this.file = plugin.getFileManager().getRollMenuFile();
        register();
    }

    private HashMap<String, String> refreshPlaceholders() {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%tickets%", String.valueOf(gPlayer.getTickets()));
        placeholders.put("%tickets_used%", String.valueOf(gPlayer.getTicketsUsed()));
        placeholders.put("%pity%", String.valueOf(gPlayer.getTillGuaranteed()));
        return placeholders;
    }

    public void register() {
        file.getConfigurationSection("ITEMS").getKeys(false).forEach(item -> {
            int slot = getItemSlot(file, "ITEMS." + item);

            if (item.equalsIgnoreCase("ROLL_PERK")) {
                setItem(slot,
                        createItem(file, "ITEMS." + item, refreshPlaceholders(), gPlayer.getName(), true),
                        p -> {
                            if (plugin.getPerkManager().checkRollPurchase(gPlayer)) {
                                updateRollPerk();
                                updatePityLuck();
                                setSword();
                            } else {
                                p.getOpenInventory().close();
                            }
                        });

            } else if (item.equalsIgnoreCase("VIEW_PERKS")) {
                setItem(slot,
                        createItem(file, "ITEMS." + item, refreshPlaceholders(), gPlayer.getName(), true),
                        p -> new PerksMenu(plugin, gPlayer).open(p));

            } else if (item.startsWith("SHOP")) {
                setItem(slot,
                        createItem(file, "ITEMS." + item, refreshPlaceholders(), gPlayer.getName(), true),
                        p -> {
                            BigDecimal cost = file.getDouble("ITEMS." + item + ".COST") <= 0
                                    ? BigDecimal.ZERO
                                    : BigDecimal.valueOf(file.getDouble("ITEMS." + item + ".COST"));
                            int tickets = file.getInt("ITEMS." + item + ".TICKETS");
                            plugin.getPerkManager().checkTicketPurchase(gPlayer, cost, tickets);
                            updateRollPerk();
                            if (plugin.getConfig().getBoolean("shop.close-on-purchase", true)) {
                                p.getOpenInventory().close();
                            }
                        });

            } else {
                setItem(slot,
                        createItem(file, "ITEMS." + item, refreshPlaceholders(), gPlayer.getName(), true));
            }
        });

        setSword();
    }

    public void setSword() {
        if (!file.getBoolean("sword.enabled")) return;

        int slot = file.getInt("sword.slot");
        setItem(slot, integration.getSwordAPI().getSwordItemFromPlayer(gPlayer.getPlayer()));
    }

    public void updatePityLuck() {
        int slot = getItemSlot(file, "ITEMS.PITY_LUCK");
        setItem(slot, createItem(file, "ITEMS.PITY_LUCK", refreshPlaceholders(), gPlayer.getName(), true));
    }

    public void updateRollPerk() {
        int slot = getItemSlot(file, "ITEMS.ROLL_PERK");
        setItem(slot, createItem(file, "ITEMS.ROLL_PERK", refreshPlaceholders(), gPlayer.getName(), true));
    }
}
