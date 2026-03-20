package me.frxq.perkroll.menu;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.datafactory.player.GPlayer;
import me.frxq.perkroll.integration.SwordProvider;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class RollMenu extends GUITemplate {
    private final PerkRoll plugin;
    private final GPlayer gPlayer;
    private final FileConfiguration file;

    public RollMenu(PerkRoll plugin, GPlayer gPlayer) {
        super(plugin,
                plugin.getFileManager().getRollMenuFile().getInt("ROWS"),
                plugin.getFileManager().getRollMenuFile().getString("TITLE"));
        this.plugin = plugin;
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
                            if (gPlayer.hasActivePerk()) {
                                if (plugin.getConfig().getStringList("confirm-rarities").contains(gPlayer.getActivePerk().getPerk().getName())) {
                                    new ConfirmMenu(plugin, gPlayer).open(p);
                                    return;
                                }
                            }

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
                            double cost = file.getDouble("ITEMS." + item + ".COST");
                            int tickets = file.getInt("ITEMS." + item + ".TICKETS");
                            String currency = file.getString("ITEMS." + item + ".CURRENCY", "rivalcredits");
                            plugin.getPerkManager().checkTicketPurchase(gPlayer, currency, cost, tickets);
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
        SwordProvider swordProvider = plugin.getIntegrationManager().getSwordProvider();
        if (swordProvider == null) return;
        int slot = file.getInt("sword.slot");
        setItem(slot, swordProvider.getSwordItem(gPlayer.getPlayer()));
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
