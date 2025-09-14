package me.frxq.perkroll.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class GUIListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        UUID playerUUID = player.getUniqueId();
        UUID inventoryUUID = GUITemplate.getOpenInventories().get(playerUUID);

        if (inventoryUUID == null) return;

        GUITemplate gui = GUITemplate.getInventoriesByUUID().get(inventoryUUID);
        if (gui == null) return;

        e.setCancelled(true);

        if (e.getClickedInventory() != player.getOpenInventory().getTopInventory()) return;

        GUITemplate.GUIAction action = gui.getActions().get(e.getSlot());
        if (action != null) {
            action.click(player);
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        removePlayerFromOpenInventories(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removePlayerFromOpenInventories(e.getPlayer().getUniqueId());
    }
    private void removePlayerFromOpenInventories(UUID playerUUID) {
        GUITemplate.getOpenInventories().remove(playerUUID);
    }
}
