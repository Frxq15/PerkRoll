package me.frxq.perkroll.menu;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.format.ColorFormatter;
import me.frxq.perkroll.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class GUITemplate {
    private static final Map<UUID, GUITemplate> inventoriesByUUID = new ConcurrentHashMap<>();
    private static final Map<UUID, UUID> openInventories = new ConcurrentHashMap<>();

    private final PerkRoll plugin;
    private final Inventory inventory;
    private final Map<Integer, GUIAction> actions = new HashMap<>();
    private final UUID uuid;

    public GUITemplate(PerkRoll plugin, int rows, String title) {
        this.plugin = plugin;
        this.uuid = UUID.randomUUID();
        this.inventory = Bukkit.createInventory(null, 9 * rows, ColorFormatter.format(title));

        inventoriesByUUID.put(uuid, this);
    }

    @FunctionalInterface
    public interface GUIAction {
        void click(Player player);
    }

    public static Map<UUID, GUITemplate> getInventoriesByUUID() {
        return inventoriesByUUID;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    public Map<Integer, GUIAction> getActions() {
        return actions;
    }
    public UUID getUUID() { return uuid; }

    public void open(Player player) {
        player.openInventory(inventory);
        openInventories.put(player.getUniqueId(), uuid);
    }

    public void delete() {
        openInventories.values().removeIf(id -> id.equals(uuid));
        inventoriesByUUID.remove(uuid);
    }

    public void setItem(int slot, ItemStack stack, GUIAction action) {
        inventory.setItem(slot, stack);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    public int getItemSlot(FileConfiguration file, String destination) {
        return file.getInt(destination + ".SLOT", 0);
    }

    public void setItem(int slot, ItemStack stack) {
        setItem(slot, stack, null);
    }

    public ItemStack createItem(Material material, String name, boolean glowing, List<String> lore, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        lore.forEach(ColorFormatter::format);
        if (meta != null) {
            meta.setDisplayName(ColorFormatter.format(name));
            lore.replaceAll(ColorFormatter::format);
            meta.setLore(lore);
            if (glowing) {
                meta.addEnchant(Enchantment.DURABILITY, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    public ItemStack createItem(FileConfiguration file, String destination, HashMap<String, String> placeholders, String playerName, boolean placeholderAPI) {
        if(file.getBoolean(destination + ".USE-TEXTURE", false)) {
            return null; //createTexturedSkullItem(file, destination, replacables, placeholderAPI, playerName);
        }
        ItemStack item = new ItemStack(Material.valueOf(file.getString(destination + ".MATERIAL", "STONE")), file.getInt(destination + ".AMOUNT", 1));
        ItemMeta meta = item.getItemMeta();
        AtomicReference<String> name = new AtomicReference<>(file.getString(destination + ".NAME", "&cInvalid Item Name"));
        placeholders.forEach((key, value) ->
                name.set(name.get().replace(key, value))
        );
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        if(placeholderAPI) {
           name.set(StringUtils.applyPlaceholders(target, name.get()));
        }
        List<String> lore = new ArrayList<>(file.getStringList(destination + ".LORE"));
        lore.replaceAll(line -> {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                line = line.replace(entry.getKey(), entry.getValue());
                if(placeholderAPI) {
                   line = StringUtils.applyPlaceholders(target, line);
                }
            }
            return ColorFormatter.format(line);
        });
        if (meta != null) {
            meta.setDisplayName(ColorFormatter.format(name.get()));
            meta.setLore(lore);
            if(file.getBoolean(destination + ".GLOW", false)) {
                meta.addEnchant(Enchantment.DURABILITY, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
