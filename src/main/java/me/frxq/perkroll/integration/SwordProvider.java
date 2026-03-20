package me.frxq.perkroll.integration;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SwordProvider {
    ItemStack getSwordItem(Player player);
}
