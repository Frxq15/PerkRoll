package me.frxq.perkroll.menu;

import me.frxq.perkroll.PerkRoll;
import me.frxq.perkroll.format.ColorFormatter;
import me.frxq.perkroll.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
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
        if(file.contains(destination + ".MODEL-DATA")) {
            return createModelDataItem(file, destination, placeholders, playerName, placeholderAPI);
        }
        if (file.getString(destination + ".MATERIAL", "STONE").equalsIgnoreCase("PLAYER_HEAD") && file.contains(destination + ".TEXTURE")) {
            return createTexturedSkullItem(file, destination, placeholders);
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
    public ItemStack createModelDataItem(FileConfiguration file, String destination, HashMap<String, String> placeholders, String playerName, boolean placeholderAPI) {
        ItemStack item = new ItemStack(Material.valueOf(file.getString(destination + ".MATERIAL", "STONE")), file.getInt(destination + ".AMOUNT", 1));
        ItemMeta meta = item.getItemMeta();
        int modelData = file.getInt(destination + ".MODEL-DATA", 0);
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
            meta.setCustomModelData(modelData);
            if(file.getBoolean(destination + ".GLOW", false)) {
                meta.addEnchant(Enchantment.DURABILITY, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    public ItemStack createTexturedSkullItem(FileConfiguration file, String destination, HashMap<String, String> replacables) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, file.getInt(destination + ".AMOUNT", 1), (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        PlayerTextures textures = profile.getTextures();
        URL l;
        try {
            l = new URL(file.getString(destination + ".TEXTURE"));
        } catch (MalformedURLException e) {
            l = null;
            plugin.error("GUI: Failed to load skull texture: " + file.getString(destination + ".TEXTURE") + " from file.");
        }
        textures.setSkin(l);
        meta.setOwnerProfile(profile);
        AtomicReference<String> name = new AtomicReference<>(file.getString(destination + ".NAME"));
        replacables.forEach((key, value) ->
                name.set(name.get().replace(key, value))
        );
        List<String> lore = new ArrayList<>(file.getStringList(destination + ".LORE"));
        lore.replaceAll(line -> {
            for (Map.Entry<String, String> entry : replacables.entrySet()) {
                line = line.replace(entry.getKey(), entry.getValue());
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
    public void setMiscItems(FileConfiguration file, HashMap<String, String> replacables, Player player, boolean placeholderAPI) {
        ConfigurationSection miscItemsSection = file.getConfigurationSection("MISC_ITEMS");
        if (miscItemsSection != null && !miscItemsSection.getKeys(false).isEmpty()) {
            miscItemsSection.getKeys(false).forEach(item -> {
                if(item.equalsIgnoreCase("RETURN")) {
                    setMenuReturnItem(getItemSlot(file, "MISC_ITEMS.RETURN"), file, player);
                    return;
                }
                if (item.equalsIgnoreCase("CLOSE_MENU")) {
                    setCloseItem(getItemSlot(file, "MISC_ITEMS.CLOSE_MENU"), file, player);
                } else {
                    setItem(getItemSlot(file, "MISC_ITEMS." + item), createItem(file, "MISC_ITEMS." + item, replacables, player.getName(), placeholderAPI), null);
                }
            });
        }
    }
    public void setMenuReturnItem(int slot, FileConfiguration config, Player player) {
        ItemStack closeItem = createMenuReturnItem(config);

        setItem(slot, closeItem, p -> {
           new RollMenu(plugin, plugin.getDataFactory().getGPlayerDataFactory().getGPlayerData(player.getUniqueId())).open(player);
        });
    }
    public void setCloseItem(int slot, FileConfiguration config, Player player) {
        ItemStack closeItem = createCloseItem(config);

        setItem(slot, closeItem, p -> {
            player.getOpenInventory().close();
        });
    }
    public ItemStack createMenuReturnItem(FileConfiguration config) {
        List<String> lore = config.getStringList("MISC_ITEMS.RETURN.LORE");
        String name = config.getString("MISC_ITEMS.RETURN.NAME");
        Material material = Material.valueOf(config.getString("MISC_ITEMS.RETURN.MATERIAL"));
        int amount = config.getInt("MISC_ITEMS.RETURN.AMOUNT", 1);
        boolean glow = config.getBoolean("MISC_ITEMS.RETURN.GLOW", false);

        return createItem(material, name, glow, lore, amount);
    }
    public ItemStack createCloseItem(FileConfiguration config) {
        List<String> lore = config.getStringList("MISC_ITEMS.CLOSE_MENU.LORE");
        String name = config.getString("MISC_ITEMS.CLOSE_MENU.NAME");
        Material material = Material.valueOf(config.getString("MISC_ITEMS.CLOSE_MENU.MATERIAL"));
        int amount = config.getInt("MISC_ITEMS.CLOSE_MENU.AMOUNT", 1);
        boolean glow = config.getBoolean("MISC_ITEMS.CLOSE_MENU.GLOW", false);

        return createItem(material, name, glow, lore, amount);
    }
}
